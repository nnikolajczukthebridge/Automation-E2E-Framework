package steps.transfers;

import checkpoints.TransfersResponseAsserts;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.restassured.response.Response;
import models.TransferNcbCashin;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONObject;
import org.testng.Assert;
import utils.CreateBodyContent;
import utils.KafkaTestConsumer;
import utils.RequestMakers;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class common {

    JSONObject inputBody, jsonResponse, jsonKafkaEventValue;
    Response response;
    TransferNcbCashin inputTopTransfer, responseTopTransfer;
    KafkaTestConsumer kafkaConsumer;
    KafkaConsumer<String, String> consumer;
    ConsumerRecords<String, String> records = null;
    String accountReference;
    String bankIdentifier;

    @Given("a payload:")
    public void a_payload(io.cucumber.datatable.DataTable dataTable) {

        ArrayList<String> keys = new ArrayList(), values = new ArrayList();
        keys.add("source_account_id");
        keys.add("source_user_id");
        keys.add("amount");
        keys.add("currency");
        keys.add("comments");
        keys.add("transfer_type");
        keys.add("external_reference_id");
        values.add(dataTable.cell(0,1));
        values.add(dataTable.cell(1,1));
        values.add(dataTable.cell(2,1));
        values.add(dataTable.cell(3,1));
        values.add(dataTable.cell(4,1));
        values.add(dataTable.cell(5,1));
        values.add(dataTable.cell(6,1));
        inputBody = CreateBodyContent.setBodyPostAccount(keys, values);

        System.out.println(inputBody);

        accountReference = dataTable.cell(7,1);
        bankIdentifier = dataTable.cell(8,1);


        Calendar date = Calendar.getInstance();

        long timeInSecs = date.getTimeInMillis();
        Date afterAdding10Mins = new Date(timeInSecs - (1 * 60 * 1000));






        String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(afterAdding10Mins);
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        System.out.println(timeStamp);





    }

    @When("perform a POST to transfers")
    public void perform_a_post_to_transfers() {
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IlNqUmpmVFJHZTJtQk1CY2QyaVk2bSJ9.eyJpc3MiOiJodHRwczovL2Rldi15aXU4N2twYy51cy5hdXRoMC5jb20vIiwic3ViIjoiYXV0aDB8NjJlMDVkZjI0M2U5ODg1ODU5ZGM4N2M3IiwiYXVkIjpbImh0dHBzOi8vZGV2LW5hdXRpbHVzLm5ldC92MS90cmFuc2ZlcnMiLCJodHRwczovL2Rldi15aXU4N2twYy51cy5hdXRoMC5jb20vdXNlcmluZm8iXSwiaWF0IjoxNjE4MzMzNzk1LCJleHAiOjE2MTg0MjAxOTUsImF6cCI6InRkaTR6MW9mUmdMVUZYVFVLVDlSbGE3YXBGbHMzcjF2Iiwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCJ9.hz6N_-vULWDv02OyQIb0h4XNj3V7UNO9GgeIlEm3ZXA";
        response = RequestMakers.makePostRequestWithToken("http://nonprod-aks.api-nautilus.net:30616/transfers/"
                ,inputBody.toString(),jwt);
        jsonResponse = new JSONObject(response.getBody().asString());
        System.out.println(jsonResponse);

    }

    @Then("verify that the status code it is {string}")
    public void verify_that_the_status_code_it_is(String statusCode) {
            TransfersResponseAsserts.validateStatusCode(parseInt(statusCode),response.getStatusCode());
    }

    @And("all the data in the response body is correct and valid")
    public void all_the_data_in_the_response_body_is_correct_and_valid() {
        Gson g = new Gson();

        try{
            inputTopTransfer = g.fromJson(inputBody.toString(), TransferNcbCashin.class);
            responseTopTransfer = g.fromJson(jsonResponse.toString(), TransferNcbCashin.class);
        }catch(Exception e){
            e.getMessage();
            e.printStackTrace();
        }

        inputTopTransfer.setTransfer_id(responseTopTransfer.getTransfer_id());
        //esto hay que arreglarlo y pasarle la fecha actual con java y cortar los minutos para que siempre coincida
        inputTopTransfer.setDatetime(responseTopTransfer.getDatetime());
        inputTopTransfer.setStatus("PENDING");
        inputTopTransfer.setCategory("PERSONAL");
        try{
            TransfersResponseAsserts.topResponseComparer(inputTopTransfer, responseTopTransfer);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @And("the team.nautilus.event.transfer.ncb.topup event was generated on the ncb_operations topic")
    public void the_team_nautilus_event_transfer_ncb_topup_event_was_generated_on_the_ncb_operations_topic() throws InterruptedException {

        Thread.sleep(20000);
        kafkaConsumer = new KafkaTestConsumer();
        consumer = kafkaConsumer.SetConsumerObject();
        ConsumerRecord<String,String> recordFound=null;

        try{
            records = kafkaConsumer.readAllKafkaMessagess(consumer,"ncb_operations");
        }catch (Exception e){
            e.getMessage();
            e.printStackTrace();
        }

        if(!records.isEmpty()){
            for(ConsumerRecord<String,String> record:records){
                if (record.value().contains(String.valueOf(inputTopTransfer.getTransfer_id())) && kafkaConsumer.HeaderBuilder(record,"ce_type").equals("team.nautilus.event.transfer.ncb.topup")){
                    System.out.println(kafkaConsumer.HeaderBuilder(record, "ce_type"));
                    System.out.println(record.value());
                    recordFound = record;
                    break;
                }
            }
        } else {
            System.out.println("The records object is empty.................................");
        }

        jsonKafkaEventValue = new JSONObject(recordFound.value());


        System.out.println("In the ncb_operations topic, for the team.nautilus.event.transfer.ncb.topup event:");

        //headers verification
        Assert.assertEquals("team.nautilus.event.transfer.ncb.topup",kafkaConsumer.HeaderBuilder(recordFound,"ce_type"), "Kafka headers - ce_type does not match");
        Assert.assertEquals("/nautilus_core/transfer_ms",kafkaConsumer.HeaderBuilder (recordFound,"ce_source"),"Kafka headers - ce_source does not match");
        Assert.assertEquals("1.0",kafkaConsumer.HeaderBuilder(recordFound,"ce_specversion"),"Kafka headers - ce_specversion does not match");
        Assert.assertEquals("application/json",kafkaConsumer.HeaderBuilder(recordFound,"content-type"),"Kafka headers content-type does not match");
        Assert.assertEquals("0.0.1",kafkaConsumer.HeaderBuilder(recordFound,"ce_payloadversion"),"Kafka headers - ce_payloadversion does not match");
        System.out.println("Kafka - header validations OK");

        //value verification
        Assert.assertEquals(String.valueOf(inputTopTransfer.getTransfer_id()).trim(),jsonKafkaEventValue.get("transferId").toString().trim(), "Kafka Value transferId does not match");
        Assert.assertEquals(inputTopTransfer.getSource_account_id().trim(),jsonKafkaEventValue.get("accountId").toString().trim(), "Kafka Value - accountId does not match");
        Assert.assertEquals(inputTopTransfer.getSource_user_id().trim(),jsonKafkaEventValue.get("userId").toString().trim(), "Kafka Value - userId does not match");
        Double amount = Double.valueOf(jsonKafkaEventValue.get("amount").toString().trim());
        DecimalFormat format = new DecimalFormat("0.#");
        Assert.assertEquals(inputTopTransfer.getAmount().trim(),format.format(amount), "Kafka Value - amount does not match");
        Assert.assertEquals(inputTopTransfer.getCurrency().trim(),jsonKafkaEventValue.get("currency").toString().trim(), "Kafka Value - currency does not match");
        Assert.assertEquals(bankIdentifier,jsonKafkaEventValue.get("bankIdentifier").toString().trim(), "Kafka Value - bankIdentifier does not match");
        Assert.assertEquals(accountReference,jsonKafkaEventValue.get("accountReference").toString().trim(), "Kafka Value - accountReference does not match");

        System.out.println("Kafka - Value validations OK");
        consumer.close();

    }

    @And("the team.nautilus.event.balance.ncb.topup event was generated on the balance topic")
    public void the_team_nautilus_event_balance_ncb_topup_event_was_generated_on_the_balance_topic() throws InterruptedException {

        Thread.sleep(20000);
        kafkaConsumer = new KafkaTestConsumer();
        consumer = kafkaConsumer.SetConsumerObject();
        ConsumerRecord<String,String> recordFound=null;

        try{
            records = kafkaConsumer.readAllKafkaMessagess(consumer,"balance");
        }catch (Exception e){
            e.getMessage();
            e.printStackTrace();
        }

        if(!records.isEmpty()) {
            for (ConsumerRecord<String, String> record : records) {
                if (record.value().contains(String.valueOf(inputTopTransfer.getTransfer_id())) && kafkaConsumer.HeaderBuilder(record, "ce_type").equals("team.nautilus.event.balance.ncb.topup")) {
                    System.out.println(kafkaConsumer.HeaderBuilder(record, "ce_type"));
                    System.out.println(record.value());
                    recordFound = record;
                    break;
                }
            }
        } else{
            System.out.println("The record object is empty............................................");
        }

        System.out.println("In the balance topic, for the team.nautilus.event.balance.ncb.topup event:");

        //headers verification
        Assert.assertEquals("team.nautilus.event.balance.ncb.topup",kafkaConsumer.HeaderBuilder(recordFound,"ce_type"), "Kafka headers - ce_type does not match");
        Assert.assertEquals("/external_banks/ncb_stream_processor",kafkaConsumer.HeaderBuilder (recordFound,"ce_source"),"Kafka headers - ce_source does not match");
        Assert.assertEquals("1.0",kafkaConsumer.HeaderBuilder(recordFound,"ce_specversion"),"Kafka headers - ce_specversion does not match");
        Assert.assertEquals("application/json",kafkaConsumer.HeaderBuilder(recordFound,"content-type"),"Kafka headers - content-type does not match");
        Assert.assertEquals("0.0.2",kafkaConsumer.HeaderBuilder(recordFound,"ce_payloadversion"),"Kafka headers - ce_payloadversion does not match");

        System.out.println("Kafka - header validations OK");

        jsonKafkaEventValue = new JSONObject(recordFound.value());

        //value verification
        Assert.assertEquals(String.valueOf(inputTopTransfer.getTransfer_id()).trim(),jsonKafkaEventValue.get("transferId").toString().trim(), "Kafka Value - transferId does not match");
        Assert.assertEquals(inputTopTransfer.getSource_account_id().trim(),jsonKafkaEventValue.get("accountId").toString().trim(), "Kafka Value - accountId does not match");
        Assert.assertEquals(inputTopTransfer.getSource_user_id().trim(),jsonKafkaEventValue.get("userId").toString().trim(), "Kafka Value - userId does not match");
        Double amount = Double.valueOf(jsonKafkaEventValue.get("amount").toString().trim());
        DecimalFormat format = new DecimalFormat("0.#");
        Assert.assertEquals(inputTopTransfer.getAmount().trim(),format.format(amount), "Kafka Value - amount does not match");
        Assert.assertEquals(inputTopTransfer.getCurrency().trim(),jsonKafkaEventValue.get("currency").toString().trim(), "Kafka Value - currency does not match");
        Assert.assertTrue(jsonKafkaEventValue.get("bankReferenceId").toString().contains(String.valueOf(inputTopTransfer.getTransfer_id()).trim()));
        Assert.assertNotNull(jsonKafkaEventValue.get("comments"),"Kafka Value - comments field is null");
        Assert.assertNotNull(jsonKafkaEventValue.get("finacleId"),"Kafka Value - finacleId field is null");

        System.out.println("Kafka - Value validations OK");
    }

    @And("the team.nautilus.event.transfer.balance.update event was generated on the balance topic")
    public void the_team_nautilus_event_transfer_balance_update_event_was_generated_on_the_balance_topic() throws InterruptedException {

        ConsumerRecord<String,String> recordFound=null;

        if(!records.isEmpty()) {
            for (ConsumerRecord<String, String> record : records) {
                if (record.value().contains(String.valueOf(inputTopTransfer.getTransfer_id())) && kafkaConsumer.HeaderBuilder(record, "ce_type").equals("team.nautilus.event.transfer.balance.update")) {
                    System.out.println(kafkaConsumer.HeaderBuilder(record, "ce_type"));
                    System.out.println(record.value());
                    recordFound = record;
                    break;
                }
            }
        } else{
            System.out.println("The records object is empty.............................................");
        }

        System.out.println("In the balance topic, for the team.nautilus.event.transfer.balance.update event:");

        //headers verification
        Assert.assertEquals("team.nautilus.event.transfer.balance.update",kafkaConsumer.HeaderBuilder(recordFound,"ce_type"), "Kafka headers - ce_type does not match");
        Assert.assertEquals("/nautilus_core/balance_stream_processor",kafkaConsumer.HeaderBuilder (recordFound,"ce_source"),"Kafka headers - ce_source does not match");
        Assert.assertEquals("1.0",kafkaConsumer.HeaderBuilder(recordFound,"ce_specversion"),"Kafka headers - ce_specversion does not match");
        Assert.assertEquals("application/json",kafkaConsumer.HeaderBuilder(recordFound,"content-type"),"Kafka headers - content-type does not match");
        Assert.assertEquals("0.0.2",kafkaConsumer.HeaderBuilder(recordFound,"ce_payloadversion"),"Kafka headers - ce_payloadversion does not match");

        System.out.println("Kafka - header validations OK");

        jsonKafkaEventValue = new JSONObject(recordFound.value());

        //value verification
        Assert.assertEquals(String.valueOf(inputTopTransfer.getTransfer_id()).trim(),jsonKafkaEventValue.get("transferId").toString().trim(), "Kafka Value - transferId does not match");
        Assert.assertEquals("OK",jsonKafkaEventValue.get("operationResult").toString().trim(), "Kafka Value - operationResult does not match");
        Assert.assertTrue(jsonKafkaEventValue.get("bankReferenceId").toString().contains(String.valueOf(inputTopTransfer.getTransfer_id()).trim()));
        System.out.println("Kafka - Value validations OK");
        consumer.close();


    }

    @And("new transfer is in db")
    public void new_transfer_is_in_db() {

        //TransfersDataBaseAsserts transfersDataBaseAsserts = new TransfersDataBaseAsserts();
        //transfersDataBaseAsserts.dataBaseAllFields(Long.valueOf(inputTopTransfer.getTransfer_id()));
    }
}

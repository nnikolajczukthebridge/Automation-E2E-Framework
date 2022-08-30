package TransfersDEPRECATED;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.CreateBodyContent;
import utils.RequestMakers;

public class HelloWorld {
    /*
    @Test
    public void createNewEtop(){
        String url = "http://nonprod-aks.api-nautilus.net:30616/transfers";
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IlNqUmpmVFJHZTJtQk1CY2QyaVk2bSJ9.eyJodHRwczovL2x5bmsudXMvdGllciI6IlZVMSIsImlzcyI6Imh0dHBzOi8vZGV2LXlpdTg3a3BjLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJhdXRoMHw2MWM0YzQ0MWUwOWM4MzAwNmYxOTk3NWEiLCJhdWQiOlsiaHR0cDovL2FwcGNvbnN1bWVyYXV0aGJmZiIsImh0dHBzOi8vbmF1dGlsdXMtcWEudXMuYXV0aDAuY29tL3VzZXJpbmZvIl0sImlhdCI6MTYxODMzMzc5NSwiZXhwIjoxNjE4NDIwMTk1LCJhenAiOiJ0ZGk0ejFvZlJnTFVGWFRVS1Q5UmxhN2FwRmxzM3IxdiIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwgcmVhZDp1c2VycyByZWFkOnByb2ZpbGVzIHJlYWQ6YWNjb3VudHMgcmVhZDp0cmFuc2ZlcnMgdXBkYXRlOmRldmljZXMgb2ZmbGluZV9hY2Nlc3MifQ.laCDCjtIkrU1txZqSXKT3Uh0AgwBa7uMNuNGiEY1tSE";
        JSONObject bodyContent = CreateBodyContent.createNewEtop("1362","auth0|61c4c441e09c83006f19975a","1","JMD","Test ETOP Tier VU1","0001","Tier ETOP test");
        Response response = RequestMakers.makePostRequestWithToken(url,bodyContent.toString(),jwt);
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == 201, "ETOP created successfully");
        JSONObject responseBody = new JSONObject(response.getBody().asString());
        Assert.assertTrue(responseBody.has("transfer_id"));
        Assert.assertTrue(responseBody.has("category"));

    }

     */
}

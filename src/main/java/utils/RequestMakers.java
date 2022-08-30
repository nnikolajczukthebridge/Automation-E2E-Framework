package utils;

import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class RequestMakers {

    public static Response makePostRequestWithToken(String url, String bodyContent, String jwt){
        Response response = given()
                .header("Authorization","Bearer " + jwt)
                .header("Content-type","application/json")
                .when()
                .body(bodyContent)
                .post(url)
                .then()
                .extract().response();
        return response;
    }
}

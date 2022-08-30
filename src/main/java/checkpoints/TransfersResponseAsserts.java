package checkpoints;

import models.TransferNcbCashin;
import org.testng.Assert;

import java.lang.reflect.Field;

public class TransfersResponseAsserts {
    public static void validateStatusCode(Integer statusCode, Integer responseCode) {
        Assert.assertEquals(statusCode, responseCode,"Server response with incorrect error message, expected "
                + statusCode+" but was " +  responseCode);
        System.out.println("Response code validation OK: "
                + responseCode + " and was expected " + statusCode);
    }

    public static void topResponseComparer(TransferNcbCashin input, TransferNcbCashin response)
            throws IllegalArgumentException, IllegalAccessException{
        for(Field field: TransferNcbCashin.class.getDeclaredFields()) {
            field.setAccessible(true);
            Assert.assertEquals(field.get(input), field.get(response),
                    "Validation between input data and response body failed "
                            + field.getName());
        }
        System.out.println("Response body validations OK");
    }
}

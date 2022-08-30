package utils;

import org.json.JSONObject;

import java.util.ArrayList;

public class CreateBodyContent {

    public static JSONObject setBodyPostAccount(ArrayList<String> keys, ArrayList<String> values){
        JSONObject jsonObject = new JSONObject();
        for(int i=0; i < keys.size(); i++){
            jsonObject
                    .accumulate(keys.get(i), values.get(i));
        }
        return jsonObject;
    }
}

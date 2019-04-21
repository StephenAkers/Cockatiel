package Cockatiel;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

class HttpRequest {

    static String executePost(String targetURL, String email, String name, int avg, Integer [] studentIds) {
        try {
            JSONObject json = new JSONObject();
            json.put("id", email);
            json.put("name", name);
            json.put("average", avg);
            json.put("studentIds", new JSONArray(Arrays.asList(studentIds)));

            HttpResponse<String> jsonResponse = Unirest.post(targetURL)
                    .body(json)
                    .asString();
            return jsonResponse.toString();
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }
}

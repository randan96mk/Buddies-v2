package com.trendit.mkg.trendit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmadikex on 8/27/2016.
 */

public class JsonProcess {

    public static List<Person> getPersonResultByString(){
        List<Person> result = new ArrayList<>();
        try {

        JSONArray jsonArray = new JSONArray("[     {       \"personId\": \"12345\",       \"latitude\": \"12.886285\",       \"longitude\": \"77.623450\"     },     {       \"personId\": \"2355\",       \"latitude\": \"12.888021\",       \"longitude\": \"77.621724\"     },     {       \"personId\": \"34243\",       \"latitude\": \"12.850565\",       \"longitude\": \"77.649002\"     },     {       \"personId\": \"873\",       \"latitude\": \"12.868398\",       \"longitude\": \"77.629059\"     },     {       \"personId\": \"554344\",       \"latitude\": \"12.912573\",       \"longitude\": \"77.611559\"     },     {       \"personId\": \"66434\",       \"latitude\": \"12.906445\",       \"longitude\": \"77.647255\"     },     {       \"personId\": \"7734\",       \"latitude\": \"12.932648\",       \"longitude\": \"77.629446\"     },     {       \"personId\": \"8854\",       \"latitude\": \"12.868658\",       \"longitude\": \"77.666127\"     },     {       \"personId\": \"9453\",       \"latitude\": \"23213\",       \"longitude\": \"12332\"     }   ] ");
        for (int i=0;i<jsonArray.length();i++) {
           JSONObject jsonObj = (JSONObject) jsonArray.get(i);
            Person person = new Person();
            person.setPersonId(jsonObj.get("personId").toString());
            person.setLatitude(Double.valueOf(jsonObj.get("latitude").toString()));
            person.setLongitude(Double.valueOf(jsonObj.get("longitude").toString()));

            result.add(person);
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}

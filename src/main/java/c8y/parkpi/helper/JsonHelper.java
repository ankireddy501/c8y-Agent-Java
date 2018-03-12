package c8y.parkpi.helper;

import c8y.parkpi.Sensor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonHelper {

    public static void copyJSONToFile(List<Sensor> sensors) {
        JSONObject jsonObject = new JSONObject();
        JSONArray sensorJsonArray = new JSONArray();
        for (Sensor sensor : sensors) {
            JSONObject simple = new JSONObject();
            simple.put("name", sensor.getName());
            simple.put("trig", sensor.getTrig());
            simple.put("echo", sensor.getEcho());
            simple.put("id", sensor.getsID());
            sensorJsonArray.add(simple);
        }

        try(FileWriter file = new FileWriter("sendmeasure.json");) {
            jsonObject.put("sensors", sensorJsonArray);
            file.write(jsonObject.toJSONString());
            file.flush();
        } catch (IOException e) {
        }
    }
}

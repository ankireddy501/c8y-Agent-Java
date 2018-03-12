package c8y.parkpi.helper;

import c8y.parkpi.Sensor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Get name,the number of parking place we have to create,trig and echo pin for reading the measurement
 */

public class PiProperties {
    public static PiProperties INSTANCE = new PiProperties();
    private String piName;
    private String piExName;
    private List<Sensor> sensors;
    /*  private JSONArray sensors*/

    private void Initialize() {
        JSONObject fObj = null;
        try {
            JSONParser parser = new JSONParser();
            FileReader r = new FileReader("./cfg/parkingpi.json");
            Object obj = parser.parse(r);
            fObj = (JSONObject) obj;

            //get piname and set it to the var
            piExName = (String) fObj.get("name");
            piName = (String) fObj.get("externalname");

            JSONArray sens = (JSONArray) fObj.get("sensors");
            for (int i = 0; i < sens.size(); i++) {
                JSONObject sensorObj = (JSONObject) sens.get(i);
                sensors.add(new Sensor((String) sensorObj.get("name"), (Integer) sensorObj.get("trig"), (Integer) sensorObj.get("echo")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPiName() {
        return piName;
    }

    public String getPiExName() {
        return piExName;
    }

    public List<Sensor> getSenors() {
        return sensors;
    }
}
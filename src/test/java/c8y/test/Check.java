package c8y.test;

import c8y.parkpi.Sensor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Check {
    public static void main(String[] args) {
        List<Sensor> sensors=new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            FileReader r = new FileReader("C:\\Users\\dje\\Desktop\\ParkingPi-Driver\\parkingpi.json");
            Object obj = parser.parse(r);
           JSONObject fObj = (JSONObject) obj;
            JSONArray sens = (JSONArray) fObj.get("sensors");
            for (int i = 0; i < sens.size(); i++) {
                JSONObject sensorobj = (JSONObject) sens.get(i);
                Integer trig= Integer.valueOf((String)sensorobj.get("trig"));
                Integer echo= Integer.valueOf((String)sensorobj.get("echo"));
                sensors.add(new Sensor((String) sensorobj.get("name"),trig,echo));
            }
                for (int j = 0; j < sensors.size(); j++) {
                    sensors.get(j).setsID("2123333");
                }
                JSONArray a=new JSONArray();
            JSONObject newsensors=new JSONObject();
        /*for(int i=0;i<sensors.size();i++) {
            JSONObject simple = new JSONObject();
            simple.put("name", sensors.get(i).getName());
            simple.put("trig", sensors.get(i).getTrig());
            simple.put("echo", sensors.get(i).getsID());
            a.add(simple);
        }
        newsensors.put("sensors",a);
                    FileWriter file = new FileWriter("C:\\Users\\dje\\Desktop\\ParkingPi-Driver\\sendmeasure.json");
            file.write(newsensors.toJSONString());
                    file.flush();
                    file.close();*/
            } catch (Exception e) {
            e.printStackTrace();
        }
        /*    public void copyJSONToFile(String[] ChildId){
       List<Sensor> sensorsCopy=PiProperties.INSTANCE.getSenors();
        for (int j = 0; j < sensorsCopy.size(); j++) {
            JSONObject copyObj = (JSONObject) sensorsCopy.get(j);
            copyObj.put("id",ChildId[j]);
        }
        JSONObject newsensors=new JSONObject();
        newsensors.put("sensors",sensorsCopy);
        try {
            FileWriter file = new FileWriter("./cfg/sendmeasure.json");
            file.write(newsensors.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
        }
    }*/



 /*   JSONObject obj = new JSONObject();
    JSONArray list = new JSONArray();
       JSONObject ss=new JSONObject();
       ss.put("name","don");
       ss.put("name","sss");

       obj.put("messages", list);

        try (FileWriter file = new FileWriter("test.json")) {

        file.write(obj.toJSONString());
        file.flush();

    }catch (Exception e) {
            e.printStackTrace();
        }*/
}
 }
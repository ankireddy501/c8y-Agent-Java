package c8y.parkpi;


import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.measurement.MeasurementValue;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.client.Platform;
import c8y.*;

import java.io.FileReader;
import java.math.BigDecimal;
import com.pi4j.io.gpio.*;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
public class MeasurementPublisher {

    public BigDecimal calc_Sensor(Integer trig, Integer echo) {
        Pin trigPin=RaspiPin.getPinByAddress(trig);
        Pin echoPin=RaspiPin.getPinByAddress(echo);
        final GpioPinDigitalOutput sensorTriggerPin = GpioFactory.getInstance().provisionDigitalOutputPin(trigPin);
        final GpioPinDigitalInput sensorEchoPin = GpioFactory.getInstance().provisionDigitalInputPin(echoPin, PinPullResistance.PULL_DOWN);
        BigDecimal distance =null;
        try {
            Thread.sleep(1000);
            sensorTriggerPin.high(); // Make trigger pin HIGH
            Thread.sleep((long) 0.01);// Delay for 10 microseconds
            sensorTriggerPin.low(); //Make trigger pin LOW

            while(sensorEchoPin.isLow()){ //Wait until the ECHO pin gets HIGH

            }
            long startTime= System.nanoTime(); // Store the surrent time to calculate ECHO pin HIGH time.
            while(sensorEchoPin.isHigh()){ //Wait until the ECHO pin gets LOW

            }
            long endTime= System.nanoTime(); // Store the echo pin HIGH end time to calculate ECHO pin HIGH time.
            distance= BigDecimal.valueOf(((((endTime-startTime)/1e3)/2) / 29.1));
            /*System.out.println("Distance :"+((((endTime-startTime)/1e3)/2) / 29.1) +" cm");*/ //Printing out the distance in cm
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return distance;
    }

    void publishMeasurement(Platform platform) {
        JSONParser parser=new JSONParser();
        Object obj=new Object();
        try {
            obj = parser.parse(new FileReader("./cfg/sendmeasure.json"));
        }catch (Exception e){
            //Proper exception handlinh
        }
        JSONObject object=(JSONObject)obj;
        JSONArray sensors=(JSONArray)object.get("sensors");
        while (true) {
            for (Object sensor : sensors) {
                JSONObject sensorObj = (JSONObject) sensor;
                String id = (String) sensorObj.get("id");
                Integer trig = (Integer) sensorObj.get("trig");
                Integer echo = (Integer) sensorObj.get("echo");
                MeasurementRepresentation sendMeasure = new MeasurementRepresentation();
                ManagedObjectRepresentation sourceMo = platform.getInventoryApi().get(GId.asGId(id));

                sendMeasure.setSource(sourceMo);
                sendMeasure.setDateTime(new DateTime());
                sendMeasure.setType("c8y_linux");
                DistanceMeasurement disMeasure = new DistanceMeasurement();
                MeasurementValue disValue = new MeasurementValue();
                disValue.setValue(calc_Sensor(trig, echo));
                disValue.setUnit("cm");
                disMeasure.setDistance(disValue);
                sendMeasure.set(disMeasure);
                platform.getMeasurementApi().create(sendMeasure);
            }
        }
    }
}
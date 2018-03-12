package c8y.parkpi;

import c8y.DistanceSensor;
import c8y.IsDevice;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.Platform;
import java.util.List;

/**
 * Creates the child devices and its identity as per the requirement.
 */
public class ManageChildDevice {

    public void createChildDevices(List<Sensor> sensors, Platform platform, String parentId) {
        sensors.forEach(sensor -> {
            ManagedObjectRepresentation managedObjectRepresentation = new ManagedObjectRepresentation();
            managedObjectRepresentation.setName(String.valueOf(sensor.getName()));
            managedObjectRepresentation.set(new IsDevice());
            managedObjectRepresentation.setType("c8y_DistanceSensor");
            managedObjectRepresentation.set(new DistanceSensor());
            managedObjectRepresentation = platform.getInventoryApi().create(managedObjectRepresentation);

            ManagedObjectReferenceRepresentation sensorMref = new ManagedObjectReferenceRepresentation();
            sensorMref.setManagedObject(managedObjectRepresentation);
            platform.getInventoryApi().getManagedObjectApi(GId.asGId(parentId)).addChildDevice(sensorMref);

            ExternalIDRepresentation SenEir = new ExternalIDRepresentation();
            SenEir.setExternalId(managedObjectRepresentation.getName());
            SenEir.setType("c8y_DistanceSensor");
            SenEir.setManagedObject(managedObjectRepresentation);
            platform.getIdentityApi().create(SenEir);

            sensor.setsID(managedObjectRepresentation.getId().getValue());
            System.out.println("created");
        });
    }
}

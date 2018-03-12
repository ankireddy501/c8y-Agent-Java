package c8y.parkpi;

/**
 * This is the main driver class
 * Initialize platform and deletes all unwanted child devices and External identity created by the Agent for the first time.
 * Initiates the new child devices Creation that are need for the parkingpi(i.e Distance Sensors as per the requirements)
 * Initiates the measurements
 */
import c8y.lx.driver.Driver;
import c8y.lx.driver.OperationExecutor;
import c8y.parkpi.helper.JsonHelper;
import c8y.parkpi.helper.PiProperties;
import com.cumulocity.model.ID;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.identity.ExternalIDCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ParkingPiDriver implements Driver {
    private static Logger logger = LoggerFactory.getLogger(ParkingPiDriver.class);
    private Platform platform;

    @Override
    public void initialize() throws Exception {
        //Do Nothing
    }

    @Override
    public void initialize(Platform platform) throws Exception {
        this.platform = platform;
    }

    @Override
    public OperationExecutor[] getSupportedOperations() {
        return new OperationExecutor[0];
    }

    @Override
    public void initializeInventory(ManagedObjectRepresentation managedObjectRepresentation) {
        String piName =  PiProperties.INSTANCE.getPiName();
        String piExName = PiProperties.INSTANCE.getPiExName();
        String id = managedObjectRepresentation.getId().getValue();
        String externalId = getExternalIdByExternalName(piExName);
        if (externalId == null || !externalId.equals(piExName)) {
            List<ManagedObjectReferenceRepresentation> l = managedObjectRepresentation.getChildDevices().getReferences();
            for (ManagedObjectReferenceRepresentation aL : l) {
                platform.getInventoryApi().delete(GId.asGId(aL.getManagedObject().getId().getValue()));
            }
            System.out.println("Child Devices Removed");
            ExternalIDCollection piExter = platform.getIdentityApi().getExternalIdsOfGlobalId(GId.asGId(id));
            List<ExternalIDRepresentation> externalIds = piExter.get().getExternalIds();
            for (ExternalIDRepresentation exr : externalIds) {
                platform.getIdentityApi().deleteExternalId(exr);
            }
            managedObjectRepresentation.setName(piName);
            ExternalIDRepresentation newPiExID = new ExternalIDRepresentation();
            newPiExID.setExternalId(piExName);
            newPiExID.setType("c8y_Serial");
            newPiExID.setManagedObject(managedObjectRepresentation);
            platform.getIdentityApi().create(newPiExID);
        }
    }

    @Override
    public void discoverChildren(ManagedObjectRepresentation managedObjectRepresentation) {
        String piExName = PiProperties.INSTANCE.getPiExName();
        String parentID = managedObjectRepresentation.getId().getValue();
        List<Sensor> sensors = PiProperties.INSTANCE.getSenors();
        String piExternalId = getExternalIdByExternalName(piExName);
        if (piExternalId != null && piExternalId.equals(piExName)) {
            ManageChildDevice manageChildDevice =new ManageChildDevice();
            manageChildDevice.createChildDevices(sensors, platform, parentID);////sensor creation and registration
            JsonHelper.copyJSONToFile(sensors);
            logger.info("Child created");
        }
    }
    @Override
    public void start() {
        MeasurementPublisher publisher=new MeasurementPublisher();
        publisher.publishMeasurement(platform);
    }

    private String getExternalIdByExternalName(String piExternalName) {
        ID id = new ID();
        id.setType("c8y_Serial");
        id.setValue(piExternalName);
        ExternalIDRepresentation externalId = platform.getIdentityApi().getExternalId(id);
        if (externalId == null) {
            return null;
        }
        return externalId.getExternalId();
}
}

package com.softwareag.parkingpi;

import c8y.lx.driver.Driver;
import c8y.lx.driver.OperationExecutor;
import c8y.lx.driver.OpsUtil;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.Platform;

public class ParkingPiShutdown implements Driver,OperationExecutor{
private Platform platform;
    @Override
    public void initialize() {

    }

    @Override
    public void initialize(Platform platform) {
this.platform=platform;
    }

    @Override
    public OperationExecutor[] getSupportedOperations() {
            return new OperationExecutor[] { this };
    }

    @Override
    public void initializeInventory(ManagedObjectRepresentation mo) {
        OpsUtil.addSupportedOperation(mo, supportedOperationType());
    }

    @Override
    public void discoverChildren(ManagedObjectRepresentation mo) {

    }

    @Override
    public void start() {

    }

    @Override
    public String supportedOperationType() {
        return "c8y_ParkingPiShutdown";
    }

    @Override
    public void execute(OperationRepresentation operation, boolean cleanup) {
        if(MeasurementPublisher.performer){
            MeasurementPublisher.performer =false;
            ManagedObjectRepresentation mo=platform.getInventoryApi().get(operation.getDeviceId());
            mo.set(new ParkingPiStatus(Status.INACTIVE));
            mo.setLastUpdated(null);
            platform.getInventoryApi().update(mo);
        }
       else{
            MeasurementPublisher.performer =true;
            ManagedObjectRepresentation mo=platform.getInventoryApi().get(operation.getDeviceId());
            mo.set(new ParkingPiStatus(Status.ACTIVE));
            mo.setLastUpdated(null);
            platform.getInventoryApi().update(mo);
        }

    }
}

package com.softwareag.parkingpi;

public class Sensor {
    private String name;
    private Integer trig;
    private Integer echo;
    private String sID;
    public Sensor(String name,Integer trig,Integer echo){
        this.name=name;
        this.trig=trig;
        this.echo=echo;
    }

    public Sensor(String name, Integer trig, Integer echo, String sID){
        this.name=name;
        this.echo=echo;
        this.trig=trig;
        this.sID=sID;
    }
    public String getName() {
        return name;
    }

    public Integer getEcho() {
        return echo;
    }

    public Integer getTrig() {
        return trig;
    }

    public String getsID() {
        return sID;
    }

    public void setEcho(Integer echo) {
        this.echo = echo;
    }

    public void setTrig(Integer trig) {
        this.trig = trig;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

}

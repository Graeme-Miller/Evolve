package com.mayorgraeme.evol.data.java;

/**
 *
 * @author graeme
 */
public class LocationData extends ActorData {

    private final char locationType;
    private final Long uuid;
    private final Long waterValue;

    public LocationData(char locationType, Long uuid, Long waterValue) {
        this.locationType = locationType;
        this.uuid = uuid;
        this.waterValue = waterValue;
    }

    public char getLocationType() {
        return locationType;
    }

    public Long getUuid() {
        return uuid;
    }

    public Long getWaterValue() {
        return waterValue;
    }
}

package com.mayorgraeme.evol.data.java;

/**
 *
 * @author graeme
 */
public class LocationData extends ActorData {

    private final char locationType;
    private final Long uuid;
    private final Long distanceToWater;

    public LocationData(char locationType, Long uuid, Long distanceToWater) {
        this.locationType = locationType;
        this.uuid = uuid;
        this.distanceToWater = distanceToWater;
    }

    public char getLocationType() {
        return locationType;
    }

    public Long getUuid() {
        return uuid;
    }

    public Long getDistanceToWater() {
        return distanceToWater;
    }
}

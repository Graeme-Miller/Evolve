package com.mayorgraeme.evol.data.java;

/**
 *
 * @author graeme
 */
public class LocationData extends ActorData {

    private final char locationType;
    private final Long uuid;

    public LocationData(char locationType, Long uuid) {
        this.locationType = locationType;
        this.uuid = uuid;
    }

    public char getLocationType() {
        return locationType;
    }

    public Long getUuid() {
        return uuid;
    }
}

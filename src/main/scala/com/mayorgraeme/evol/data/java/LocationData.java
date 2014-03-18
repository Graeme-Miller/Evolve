package com.mayorgraeme.evol.data.java;

/**
 *
 * @author graeme
 */
public class LocationData extends ActorData{
    private final char locationType;

    public LocationData(char locationType) {
        this.locationType = locationType;
    }

    public char getLocationType() {
        return locationType;
    }
}

package com.mayorgraeme.evol.data.java;

/**
 *
 * @author graeme
 */
public class DeathData extends ActorData {

    private final long uuid;
    private final String species, clazz;
    private final char gender;
    private final int ageCurrent = 999999999;

    public DeathData(long uuid, String species, String clazz, char gender) {
        this.uuid = uuid;
        this.species = species;
        this.clazz = clazz;
        this.gender = gender;
    }

    public long getUuid() {
        return uuid;
    }

    public String getSpecies() {
        return species;
    }

    public String getClazz() {
        return clazz;
    }

    public char getGender() {
        return gender;
    }

    public int getAgeCurrent() {
        return ageCurrent;
    }

    
}

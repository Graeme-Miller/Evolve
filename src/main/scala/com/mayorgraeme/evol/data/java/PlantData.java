package com.mayorgraeme.evol.data.java;

/**
 *
 * @author graeme
 */
public class PlantData extends ActorData {

    private final long uuid;
    private final String species;
    private final char gender;
    private final int maxAge, ageCurrent, sproutTime, size, seedRadius, spermRadius, chanceOfPropogation, chanceOfBreeding;

    public PlantData(long uuid, String species, char gender, int ageCurrent, int maxAge, int sproutTime, int size, int seedRadius, int spermRadius, int chanceOfPropogation, int chanceOfBreeding) {
        this.uuid = uuid;
        this.species = species;
        this.gender = gender;
        
        this.ageCurrent = ageCurrent;
        this.maxAge = maxAge;
        this.sproutTime = sproutTime;
        this.size = size;
        this.seedRadius = seedRadius;
        this.spermRadius = spermRadius;
        this.chanceOfPropogation = chanceOfPropogation;
        this.chanceOfBreeding = chanceOfBreeding;
    }

    public long getUuid() {
        return uuid;
    }

    public String getSpecies() {
        return species;
    }

    public char getGender() {
        return gender;
    }

    public int getAgeCurrent() {
        return ageCurrent;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getSproutTime() {
        return sproutTime;
    }

    public int getSize() {
        return size;
    }

    public int getSeedRadius() {
        return seedRadius;
    }

    public int getSpermRadius() {
        return spermRadius;
    }

    public int getChanceOfPropogation() {
        return chanceOfPropogation;
    }

    public int getChanceOfBreeding() {
        return chanceOfBreeding;
    }
}

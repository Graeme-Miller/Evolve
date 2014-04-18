package com.mayorgraeme.evol.data.java;

/**
 *
 * @author graeme
 */
public class PlantData extends ActorData {

    private final long uuid;
    private final String species;
    private final int ageCurrent, vegativePropagation, plantSustanance, maxSize, growthRate;

    public PlantData(long uuid, String species, int ageCurrent, int vegativePropagation, int plantSustanance, int maxSize, int growthRate) {
        this.uuid = uuid;
        this.species = species;
        this.vegativePropagation = vegativePropagation;
        this.plantSustanance = plantSustanance;
        this.maxSize = maxSize;
        this.growthRate = growthRate;
        this.ageCurrent = ageCurrent;
    }

    public int getAgeCurrent() {
        return ageCurrent;
    }

    public long getUuid() {
        return uuid;
    }

    public String getSpecies() {
        return species;
    }

    public int getVegativePropagation() {
        return vegativePropagation;
    }

    public int getPlantSustanance() {
        return plantSustanance;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getGrowthRate() {
        return growthRate;
    }
}

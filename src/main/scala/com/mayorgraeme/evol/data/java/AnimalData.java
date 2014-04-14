package com.mayorgraeme.evol.data.java;

/**
 *
 * @author graeme
 */
public class AnimalData extends ActorData {

    private final long uuid;
    private final String species;
    private final char gender;
    private final boolean pregnant;
    private final int pregnancyCountdown, hunger, thirst, sex, foodSize, waterSize, partnerSize, ageCurrent, ageMax;

    public AnimalData(long uuid, String species, char gender, boolean pregnant, int pregnancyCountdown, int hunger, int thirst, int sex, int foodSize, int waterSize, int partnerSize, int ageCurrent, int ageMax) {
        this.uuid = uuid;
        this.species = species;
        this.gender = gender;
        this.pregnant = pregnant;
        this.pregnancyCountdown = pregnancyCountdown;
        this.hunger = hunger;
        this.thirst = thirst;
        this.sex = sex;
        this.foodSize = foodSize;
        this.waterSize = waterSize;
        this.partnerSize = partnerSize;
        this.ageCurrent = ageCurrent;
        this.ageMax = ageMax;
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

    public boolean isPregnant() {
        return pregnant;
    }

    public int getPregnancyCountdown() {
        return pregnancyCountdown;
    }

    public int getHunger() {
        return hunger;
    }

    public int getThirst() {
        return thirst;
    }

    public int getSex() {
        return sex;
    }

    public int getFoodSize() {
        return foodSize;
    }

    public int getWaterSize() {
        return waterSize;
    }

    public int getPartnerSize() {
        return partnerSize;
    }

    public int getAgeCurrent() {
        return ageCurrent;
    }

    public int getAgeMax() {
        return ageMax;
    }
}

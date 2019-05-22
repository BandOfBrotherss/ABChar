package com.example.abchar;

import java.util.Map;

public class FireModel {

    private String name;
    private int age, usageTime, failTrials, succesTrials, trainingCount;
    private Map trueFalse;


    public FireModel() {}


    public FireModel(String name, int age, int usageTime, Map trueFalse, int failTrials, int succesTrials, int trainingCount) {
        this.name = name;
        this.age = age;
        this.usageTime = usageTime;
        this.trueFalse = trueFalse;
        this.succesTrials = succesTrials;
        this.failTrials = failTrials;
        this.trainingCount = trainingCount;
    }

    public int getFailTrials() {
        return failTrials;
    }

    public void setFailTrials(int failTrials) {
        this.failTrials = failTrials;
    }

    public int getSuccesTrials() {
        return succesTrials;
    }

    public void setSuccesTrials(int succesTrials) {
        this.succesTrials = succesTrials;
    }

    public int getTrainingCount() {
        return trainingCount;
    }

    public void setTrainingCount(int trainingCount) {
        this.trainingCount = trainingCount;
    }

    public Map getTrueFalse() {
        return trueFalse;
    }

    public void setTrueFalse(Map trueFalse) {
        this.trueFalse = trueFalse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(int usageTime) {
        this.usageTime = usageTime;
    }
}

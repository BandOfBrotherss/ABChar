package com.example.abchar;

public class FireModel {

    private String name;
    private int age;
    private int usageTime;

    public FireModel() {}

    public FireModel(String name, int age, int usageTime) {
        this.name = name;
        this.age = age;
        this.usageTime = usageTime;
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

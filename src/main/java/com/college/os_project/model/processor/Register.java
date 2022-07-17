package com.college.os_project.model.processor;

public class Register {
    private final String name;
    private String address;
    private int value;
    private String strValue;

    public Register(String name, String addresss, int value) {
        this.name = name;
        this.address = addresss;
        this.value = value;
    }

    public Register(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public Register(String name, String value) {
        this.name = name;
        this.strValue = value;
    }

    public void incValue(int value) {
        this.value += value;
    }

    public void decValue(int value) {
        this.value -= value;
    }

    public void mulValue(int value) {this.value *= value;}

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getStrValue() {
        return this.strValue;
    }

    public void setStrValue(String instruction) {
        this.strValue = instruction;
    }

    @Override
    public String toString() {
        return this.name + " value = " + this.value;
    }
}

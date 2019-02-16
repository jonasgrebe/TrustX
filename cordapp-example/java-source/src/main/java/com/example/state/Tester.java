package com.example.state;

import com.google.gson.Gson;

public class Tester {
    private final float a = 1.0f;
    private final String s = "This is cool";
    private final Float myFloat = new Float(10);

    public String serialize(){
        return new Gson().toJson(this);
    }

    public Float getMyFloat() {
        return myFloat;
    }

    public float getA() {
        return a;
    }

    public String getS() {
        return s;
    }

    public static Tester deserialize(String jsonString){
        return new Gson().fromJson(jsonString, Tester.class);
    }
}

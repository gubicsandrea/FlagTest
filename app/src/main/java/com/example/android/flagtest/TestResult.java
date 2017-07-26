package com.example.android.flagtest;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Hadiné on 2017.07.24..
 */

/**
 * Stores 1 game result with user name and points
 */
public class TestResult implements Comparable<TestResult>, Serializable {
    //The name of the user
    private String mName;
    //Points of the user
    private int mPoints;

    public TestResult(String mName, int mPoints) {
        this.mName = mName;
        this.mPoints = mPoints;
    }

    public String getName() {
        return mName;
    }

    public int getPoints() {
        return mPoints;
    }

    //The method that enables to sort results in descending order of points
    @Override
    public int compareTo(@NonNull TestResult o) {
        return o.getPoints() - this.mPoints;
    }
}

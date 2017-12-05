package com.braingroom.user.utils;

/**
 * Created by godara on 30/11/17.
 */

public enum ClassSchedule {
    Flexible("Flexible", 1), Fixed("Fixed", 2);
    public final String name;
    public final int id;

    ClassSchedule(String name, int id) {
        this.name = name;
        this.id = id;
    }
}

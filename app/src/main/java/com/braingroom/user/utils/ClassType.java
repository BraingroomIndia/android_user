package com.braingroom.user.utils;

/**
 * Created by godara on 30/11/17.
 */

public enum ClassType {
    WORKSHOP("Workshops", 1),
    SEMINAR("Online Classes", 2),
    WEBINAR("Webinar", 3),
    CLASSES("Classes", 4),
    ACTIVITY("Learning events & activities", 5);
    public final String name;
    public final int id;

    ClassType(String name, int id) {
        this.name = name;
        this.id = id;
    }
}

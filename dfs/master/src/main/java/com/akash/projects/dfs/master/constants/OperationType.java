package com.akash.projects.dfs.master.constants;

public enum OperationType {

    /*
    public static byte UNKNOWN = 0;
    public static byte CREATE_FILE = 1;
    public static byte DELETE_FILE = 2;
    public static byte CREATE_CHUNK = 3;
    public static byte DELETE_CHUNK = 4;
    public static byte UPDATE_CHUNK = 5;
    public static byte UPDATE_NODE = 6;
    public static byte REMOVE_NODE = 7; */

    UNKNOWN("unknown"), CREATE_FILE("create-file"), DELETE_FILE("delete-file"),
    CREATE_CHUNK("create-chunk"), DELETE_CHUNK("delete-chunk"), UPDATE_NODE("update-node"),
    REMOVE_NODE("remove-node");

    private String type;

    OperationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

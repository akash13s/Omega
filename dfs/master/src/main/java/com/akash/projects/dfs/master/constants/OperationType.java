package com.akash.projects.dfs.master.constants;

public enum OperationType {

    UNKNOWN("UNKNOWN"), CREATE_FILE("CREATE_FILE"), DELETE_FILE("DELETE_FILE"),
    CREATE_CHUNK("CREATE_CHUNK"), DELETE_CHUNK("DELETE_CHUNK"), UPDATE_NODE("UPDATE_NODE"),
    REMOVE_NODE("REMOVE_NODE");

    private String type;

    OperationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

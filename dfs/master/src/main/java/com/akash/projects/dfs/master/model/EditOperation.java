package com.akash.projects.dfs.master.model;

public class EditOperation {

    private String date;
    private String operationType;
    private Object[] arguments;

    public EditOperation(String date, String operationType, Object[] arguments) {
        this.date = date;
        this.operationType = operationType;
        this.arguments = arguments;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

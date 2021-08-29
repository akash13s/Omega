package com.akash.projects.dfs.master.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditOperation implements Serializable {

    private String date;
    private String operationType;
    private List<Object> arguments;

    public EditOperation() {
        setArguments(null);
    }

    public EditOperation(String date, String operationType, Object[] arguments) {
        this.date = date;
        this.operationType = operationType;
        setArguments(arguments);
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Object[] getArguments() {
        if (arguments == null) {
            return new Object[0];
        }
        return arguments.toArray();
    }

    public void setArguments(Object[] arguments) {
        if (arguments == null) {
            this.arguments = new ArrayList<>();
        }
        else {
            this.arguments = Arrays.asList(arguments);
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

package com.akash.projects.dfs.master.utils;

import com.akash.projects.dfs.master.model.EditOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EditLogger {

    private String logPath;
    private ObjectMapper mapper;
    private ObjectWriter writer;
    private BufferedWriter fileWriter;

    public EditLogger(String logPath) throws IOException {
        this.logPath = logPath;
        this.mapper = new ObjectMapper();
        this.writer = this.mapper.writer();
        this.fileWriter = new BufferedWriter(new FileWriter(this.logPath, true));
    }

    public synchronized void addOperation(EditOperation editOperation) throws IOException {
        String s = writer.writeValueAsString(editOperation);
        fileWriter.append(s);
        fileWriter.newLine();
        fileWriter.flush();
    }
}

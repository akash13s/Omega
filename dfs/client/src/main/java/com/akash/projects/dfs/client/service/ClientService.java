package com.akash.projects.dfs.client.service;

import java.io.IOException;

public interface ClientService {

    public void loadFile(String localFilePath, int replicas, boolean isTextFile, long blockSize, long lineCount) throws IOException;

    public void getFile(String fileName);

    public void listFiles();

    public void deleteFile(String fileName);
}

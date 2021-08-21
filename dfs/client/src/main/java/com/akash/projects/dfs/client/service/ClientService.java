package com.akash.projects.dfs.client.service;

public interface ClientService {

    public void putFile(String localFilePath, int replicas, boolean isTextFile);

    public void getFile(String fileName);

    public void listFiles();

    public void deleteFile(String fileName);
}

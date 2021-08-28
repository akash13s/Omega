package com.akash.projects.dfs.client.service;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface ClientService {

    public void loadFile(String localFilePath, int replicas, boolean isTextFile, long blockSize, long lineCount) throws IOException;

    public void getFile(String fileName) throws IOException, NotBoundException;

    public void listFiles() throws RemoteException;

    public void deleteFile(String fileName) throws IOException;
}

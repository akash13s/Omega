package com.akash.projects.dfs.master.service;

import com.akash.projects.common.dfs.model.DfsChunk;
import com.akash.projects.common.dfs.model.DfsFile;
import com.akash.projects.common.dfs.model.DfsNode;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MasterService extends Remote {

    public DfsNode updateDfsNode(String registryHost, int registryPort, String serviceName, boolean writeLog) throws IOException;

    public DfsFile createFile(String fileName, int replicas, boolean writeLog) throws IOException;

    public void deleteFile(String fileName, boolean writeLog) throws IOException;

    public DfsChunk createChunk(long fileId, long offset, int size, int actualSize, boolean writeLog) throws IOException;

    public DfsFile getFile(String fileName) throws RemoteException;

    public List<String> listFiles() throws RemoteException;
}

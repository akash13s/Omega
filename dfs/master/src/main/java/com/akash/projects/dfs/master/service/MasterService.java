package com.akash.projects.dfs.master.service;

import com.akash.projects.common.dfs.model.DfsChunk;
import com.akash.projects.common.dfs.model.DfsFile;
import com.akash.projects.common.dfs.model.DfsNode;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MasterService extends Remote {

    public DfsNode updateDfsNode(String registryHost, int registryPort, String serviceName) throws RemoteException;

    public DfsFile createFile(String fileName, int replicas) throws RemoteException;

    public void deleteFile(String fileName) throws RemoteException;

    public DfsChunk createChunk(long fileId, long offset, int size) throws RemoteException;

    public DfsFile getFile(String fileName) throws RemoteException;

    public List<String> listFiles() throws RemoteException;
}

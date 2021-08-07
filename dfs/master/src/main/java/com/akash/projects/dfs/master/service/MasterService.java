package com.akash.projects.dfs.master.service;

import com.akash.projects.common.dfs.model.DfsFile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MasterService extends Remote {

    public void addDfsNode(String registryHost, int registryPort, String serviceName) throws RemoteException;

    public void createFile(String fileName, int replicas) throws RemoteException;

    public void deleteFile(long fileId) throws RemoteException;

    public boolean createChunk(long fileId, long offset, int size) throws RemoteException;

    public DfsFile getFile(String fileName) throws RemoteException;

    public List<DfsFile> listFiles() throws RemoteException;
}

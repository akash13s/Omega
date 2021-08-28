package com.akash.projects.dfs.master.service;

import com.akash.projects.common.dfs.model.DfsChunk;
import com.akash.projects.common.dfs.model.DfsFile;
import com.akash.projects.common.dfs.model.DfsNode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class MasterServiceImpl extends UnicastRemoteObject implements MasterService {

    private DfsMetaData dfsMetaData;

    public MasterServiceImpl() throws RemoteException {
        super();
        this.dfsMetaData = new DfsMetaData();
    }

    @Override
    public DfsNode updateDfsNode(String registryHost, int registryPort, String serviceName) throws RemoteException{
        return dfsMetaData.updateDfsNode(registryHost, registryPort, serviceName);
    }

    @Override
    public DfsFile createFile(String fileName, int replicas) throws RemoteException {
        return dfsMetaData.createFile(fileName, replicas);
    }

    @Override
    public void deleteFile(String fileName) throws RemoteException {
        dfsMetaData.deleteFile(fileName);
    }

    @Override
    public DfsChunk createChunk(long fileId, long offset, int size, int actualSize) throws RemoteException {
        return dfsMetaData.createChunk(fileId, offset, size, actualSize);
    }

    @Override
    public DfsFile getFile(String fileName) throws RemoteException {
        return dfsMetaData.getFile(fileName);
    }

    @Override
    public List<String> listFiles() throws RemoteException {
        return dfsMetaData.listFiles();
    }

}

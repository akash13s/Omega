package com.akash.projects.dfs.master.service;

import com.akash.projects.common.dfs.model.DfsChunk;
import com.akash.projects.common.dfs.model.DfsFile;
import com.akash.projects.common.dfs.model.DfsNode;
import com.akash.projects.dfs.master.utils.EditLogger;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class MasterServiceImpl extends UnicastRemoteObject implements MasterService {

    private DfsMetaData dfsMetaData;

    public MasterServiceImpl(EditLogger editLogger) throws RemoteException {
        super();
        this.dfsMetaData = new DfsMetaData(editLogger);
    }

    @Override
    public DfsNode updateDfsNode(String registryHost, int registryPort, String serviceName, boolean writeLog)
            throws IOException {
        return dfsMetaData.updateDfsNode(registryHost, registryPort, serviceName, writeLog);
    }

    @Override
    public DfsFile createFile(String fileName, int replicas, boolean writeLog)
            throws IOException {
        return dfsMetaData.createFile(fileName, replicas, writeLog);
    }

    @Override
    public void deleteFile(String fileName, boolean writeLog) throws IOException {
        dfsMetaData.deleteFile(fileName, writeLog);
    }

    @Override
    public DfsChunk createChunk(long fileId, long offset, int size, int actualSize, boolean writeLog)
            throws IOException {
        return dfsMetaData.createChunk(fileId, offset, size, actualSize, writeLog);
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

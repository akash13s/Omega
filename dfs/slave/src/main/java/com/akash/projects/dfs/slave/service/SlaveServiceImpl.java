package com.akash.projects.dfs.slave.service;

import com.akash.projects.dfs.slave.DFSSlave;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SlaveServiceImpl extends UnicastRemoteObject implements SlaveService{

    private DFSSlave slave;

    public SlaveServiceImpl(DFSSlave dfsSlave) throws RemoteException {
        this.slave = dfsSlave;
    }

    @Override
    public byte[] readChunk(long chunkId, long offset, int size) throws IOException {
        return slave.read(chunkId, offset, size, true);
    }

    @Override
    public boolean writeChunk(long chunkId, long offset, int size, byte[] data) throws RemoteException {
        return slave.write(chunkId, offset, size, data, false);
    }

    @Override
    public boolean deleteChunk(long chunkId) throws RemoteException{
        return slave.delete(chunkId, false);
    }
}

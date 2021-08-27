package com.akash.projects.common.dfs.service;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SlaveService extends Remote {

    public byte[] readChunk(long chunkId, long offset, int size) throws IOException;

    public boolean writeChunk(long chunkId, long offset, int size, byte[] data) throws RemoteException;

    public boolean deleteChunk(long chunkId) throws RemoteException;

    public boolean heartbeat() throws RemoteException;
}

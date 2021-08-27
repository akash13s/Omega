package com.akash.projects.dfs.master.service;

import com.akash.projects.common.dfs.model.DfsChunk;
import com.akash.projects.common.dfs.model.DfsNode;
import com.akash.projects.common.dfs.service.SlaveService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoveChunkService implements Runnable{

    private DfsChunk dfsChunk;

    public RemoveChunkService(DfsChunk dfsChunk) {
        this.dfsChunk = dfsChunk;
    }

    private SlaveService getSlaveService(DfsNode node) throws RemoteException, NotBoundException {
        Registry slaveRegistry = LocateRegistry.getRegistry(node.getRegistryHost(), node.getRegistryPort());
        return (SlaveService) slaveRegistry.lookup(node.getServiceName());
    }

    @Override
    public void run() {
        dfsChunk.getNodes().forEach(node -> {
            try {
                SlaveService slaveService = getSlaveService(node);
                slaveService.deleteChunk(dfsChunk.getId());
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
                System.out.println("Error deleting chunk!");
            }
        });
    }
}

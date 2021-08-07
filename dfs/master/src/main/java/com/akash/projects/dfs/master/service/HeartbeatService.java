package com.akash.projects.dfs.master.service;

import com.akash.projects.common.dfs.model.DfsNode;
import com.akash.projects.dfs.slave.service.SlaveService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class HeartbeatService implements Runnable{

    public HeartbeatService() {
    }

    @Override
    public void run() {
        System.out.println("starting heartbeat check");
        Map<Long, DfsNode> nodeMap = new HashMap<>(DfsMetaData.getNodeMap());
        nodeMap.values().forEach(dfsNode -> {
            try {
                Registry registry = LocateRegistry.getRegistry(dfsNode.getRegistryHost(), dfsNode.getRegistryPort());
                SlaveService slave = (SlaveService) registry.lookup(dfsNode.getServiceName());
                boolean heartbeat = slave.heartbeat();
                if (!heartbeat) {
                    DfsMetaData.removeDfsNode(dfsNode.getRegistryHost(), dfsNode.getRegistryPort(), dfsNode.getServiceName());
                }
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
                DfsMetaData.removeDfsNode(dfsNode.getRegistryHost(), dfsNode.getRegistryPort(), dfsNode.getServiceName());
            }
        });
    }

}

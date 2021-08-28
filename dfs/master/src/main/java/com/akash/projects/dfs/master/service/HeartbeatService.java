package com.akash.projects.dfs.master.service;

import com.akash.projects.common.dfs.model.DfsNode;
import com.akash.projects.dfs.master.constants.MasterConstants;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HeartbeatService implements Runnable{

    public HeartbeatService() {
    }

    @Override
    public void run() {
        System.out.println("starting heartbeat check");
        Map<Long, DfsNode> nodeMap = new HashMap<>(DfsMetaData.getNodeMap());
        Date currentDate = new Date();
        nodeMap.values().forEach(dfsNode -> {
            long diffInMillis = Math.abs(currentDate.getTime() - dfsNode.getLastActiveDate().getTime());
            if (diffInMillis > MasterConstants.DEFAULT_TIME_PERIOD) {
                System.out.println("Removing " + dfsNode.getServiceName() + " : heartbeat NOT OK");
                try {
                    DfsMetaData.removeDfsNode(dfsNode.getRegistryHost(), dfsNode.getRegistryPort(), dfsNode.getServiceName(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error removing node with serviceName: " + dfsNode.getServiceName());
                }
            }
            else {
                System.out.println(dfsNode.getServiceName() + " : heartbeat OK");
            }
        });
    }

}

package com.akash.projects.dfs.slave.service;

import com.akash.projects.common.dfs.Utils;
import com.akash.projects.dfs.master.service.MasterService;
import com.akash.projects.dfs.slave.DFSSlave;

import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class HeartbeatService implements Runnable{

    private DFSSlave slave;
    private Registry masterRegistry;

    public HeartbeatService(DFSSlave slave, Registry masterRegistry) {
        this.slave = slave;
        this.masterRegistry = masterRegistry;
    }

    @Override
    public void run() {
        try {
            MasterService masterService = (MasterService) masterRegistry.lookup(MasterService.class.getCanonicalName());
            masterService.updateDfsNode(Utils.getHost(), slave.getRegistryPort(), slave.getServiceName());
        } catch (RemoteException | NotBoundException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
}

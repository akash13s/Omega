package com.akash.projects.dfs.master;

import com.akash.projects.common.dfs.Utils;
import com.akash.projects.dfs.master.service.MasterService;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DummyClient {

    public static void main(String[] args) throws IOException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(Utils.getHost(), 8085);
        MasterService masterService = (MasterService) registry.lookup(MasterService.class.getCanonicalName());
        masterService.updateDfsNode("localhost", 8880, "dummy-worker");
    }
}

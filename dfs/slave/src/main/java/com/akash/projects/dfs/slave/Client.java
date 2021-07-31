package com.akash.projects.dfs.slave;

import com.akash.projects.common.Utils;
import com.akash.projects.dfs.slave.service.SlaveService;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) throws IOException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(Utils.getHost(), 5000);
        SlaveService slave = (SlaveService) registry.lookup("slave1");
        byte[] data = slave.readChunk(0, 0, 12);
        String str = new String(data);
        System.out.println(str);
    }
}

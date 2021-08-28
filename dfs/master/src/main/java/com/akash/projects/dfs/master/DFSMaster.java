package com.akash.projects.dfs.master;

import com.akash.projects.common.dfs.Utils;
import com.akash.projects.common.dfs.constants.MasterRegistryInfo;
import com.akash.projects.dfs.master.constants.MasterConstants;
import com.akash.projects.dfs.master.service.HeartbeatService;
import com.akash.projects.dfs.master.service.MasterService;
import com.akash.projects.dfs.master.service.MasterServiceImpl;
import com.akash.projects.dfs.master.utils.EditLogger;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DFSMaster {

    @Parameter(names = {"--master-registry-host", "-mrh"}, description = "Master registry host")
    private String masterRegistryHost = MasterRegistryInfo.DEFAULT_REGISTRY_HOST;

    @Parameter(names = {"--master-registry-port", "-mrp"}, description = "Master registry port")
    private int masterRegistryPort = MasterRegistryInfo.DEFAULT_REGISTRY_PORT;

    @Parameter(names = {"--log-path", "-lp"}, description = "Master log path")
    private String logPath = MasterConstants.DEFAULT_LOG_PATH;

    private MasterService masterService;

    private ScheduledExecutorService scheduledExecutorService;

    private EditLogger editLogger;

    public String getMasterRegistryHost() {
        return masterRegistryHost;
    }

    public int getMasterRegistryPort() {
        return masterRegistryPort;
    }

    public String getLogPath() {
        return logPath;
    }

    private void start() throws IOException {
        LocateRegistry.createRegistry(masterRegistryPort);
        editLogger = new EditLogger(logPath);
        masterService = new MasterServiceImpl(editLogger);
        Registry registry = LocateRegistry.getRegistry(Utils.getHost(), masterRegistryPort);
        registry.rebind(MasterService.class.getCanonicalName(), masterService);
        scheduledExecutorService = Executors.newScheduledThreadPool(MasterConstants.DEFAULT_THREAD_POOL_SIZE);
        scheduledExecutorService.scheduleAtFixedRate(new HeartbeatService(), 0,
                MasterConstants.DEFAULT_TIME_PERIOD, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {

        DFSMaster master = new DFSMaster();
        JCommander commander = JCommander.newBuilder()
                .addObject(master)
                .build();
        commander.parse(args);

        try {
            master.start();
            System.out.println("Master service started on port: " + master.getMasterRegistryPort());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}

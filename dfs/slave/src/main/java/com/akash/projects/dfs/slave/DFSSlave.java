package com.akash.projects.dfs.slave;

import com.akash.projects.common.dfs.Utils;
import com.akash.projects.common.dfs.constants.MasterRegistryInfo;
import com.akash.projects.dfs.master.service.MasterService;
import com.akash.projects.dfs.slave.constants.SlaveConstants;
import com.akash.projects.dfs.slave.service.HeartbeatService;
import com.akash.projects.dfs.slave.service.SlaveService;
import com.akash.projects.dfs.slave.service.SlaveServiceImpl;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DFSSlave {

    @Parameter(names = {"--master-registry-host", "-mrh"}, description = "Master registry host")
    private String masterRegistryHost = MasterRegistryInfo.DEFAULT_REGISTRY_HOST;

    @Parameter(names = {"--master-registry-port", "-mrp"}, description = "Master registry port")
    private int masterRegistryPort = MasterRegistryInfo.DEFAULT_REGISTRY_PORT;

    @Parameter(names = {"--registry-port", "-rp"}, description = "Registry port of slave")
    private int registryPort = SlaveConstants.DEFAULT_SLAVE_REGISTRY_PORT;

    @Parameter(names = {"--service-name", "-sn"}, description = "Service name of slave", required = true)
    private String serviceName;

    @Parameter(names = {"--heartbeat-period", "-hbp"}, description = "Heart beat period")
    private int heartbeatPeriod = SlaveConstants.HEARTBEAT_PERIOD;

    @Parameter(names = {"--slave-data-path", "-sdp"}, description = "Slave data path")
    private String slaveDataPath = SlaveConstants.SLAVE_DATA_PATH;

    private SlaveService slaveService;

    private MasterService masterService;

    private ScheduledExecutorService scheduledExecutorService;

    public String getMasterRegistryHost() {
        return masterRegistryHost;
    }

    public int getMasterRegistryPort() {
        return masterRegistryPort;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getHeartbeatPeriod() {
        return heartbeatPeriod;
    }

    public String getSlaveDataPath() {
        return slaveDataPath;
    }

    public int getRegistryPort() {
        return registryPort;
    }

    public byte[] read(long chunkId, long offset, int size, boolean isTest) throws IOException {
        RandomAccessFile file = new RandomAccessFile(getFilePath(chunkId, isTest), "r");
        file.seek(offset);
        byte[] bytes = new byte[size];
        file.read(bytes, (int) offset, size);
        file.close();
        return bytes;
    }

    public boolean write(long chunkId, long offset, int size, byte[] data, boolean isTest) {
        try {
            RandomAccessFile file = new RandomAccessFile(getFilePath(chunkId, isTest), "rw");
            file.seek(offset);
            file.write(data, (int) offset, Math.min(data.length, size));
            file.write(data);
            file.close();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(long chunkId, boolean isTest) {
        File file = new File(getFilePath(chunkId, isTest));
        return file.delete();
    }

    private String getFilePath(long chunkId, boolean isTest) {
        String baseDataPath;
        if (isTest) {
            baseDataPath = SlaveConstants.SLAVE_TEST_DATA_PATH;
        }
        else {
            baseDataPath = slaveDataPath;
        }
        return baseDataPath + System.getProperty("file.separator") + SlaveConstants.CHUNK_PREFIX + chunkId;
    }

    private void start() throws RemoteException, UnknownHostException, NotBoundException {
        File file = new File(slaveDataPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        LocateRegistry.createRegistry(registryPort);
        slaveService = new SlaveServiceImpl(this);
        Registry registry = LocateRegistry.getRegistry(Utils.getHost(), registryPort);
        registry.rebind(serviceName, slaveService);

        Registry masterRegistry = LocateRegistry.getRegistry(masterRegistryHost, masterRegistryPort);
        masterService = (MasterService) masterRegistry.lookup(MasterService.class.getCanonicalName());
        scheduledExecutorService = Executors.newScheduledThreadPool(SlaveConstants.DEFAULT_THREAD_POOL_SIZE);
        scheduledExecutorService.scheduleAtFixedRate(new HeartbeatService(this, masterRegistry), 0,
                SlaveConstants.HEARTBEAT_PERIOD, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        DFSSlave slave = new DFSSlave();
        JCommander commander = JCommander.newBuilder()
                .addObject(slave)
                .build();
        commander.parse(args);
        try {
            slave.start();
            System.out.println(slave.getServiceName() + " has started on port: " + slave.registryPort);
        }
        catch (Exception e) {
            System.exit(-1);
        }
    }
}

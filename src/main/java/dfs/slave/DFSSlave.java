package dfs.slave;

import service.SlaveService;
import service.SlaveServiceImpl;

public class DFSSlave {

    private String masterRegistryHost;
    private String masterRegistryPort;
    private String serviceName;
    private long heartbeatPeriod;
    private String slaveDataPath;

    private SlaveService slaveService;

    private void start() {
        slaveService = new SlaveServiceImpl(this);
    }

    public byte[] read(long chunkId, long offset, int size) {
        return new byte[0];
    }

    public boolean write(long chunkId, long offset, int size, byte[] data) {
        return false;
    }

    public boolean delete(long chunkId) {
        return false;
    }

}

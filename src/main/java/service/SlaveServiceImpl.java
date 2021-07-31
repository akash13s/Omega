package service;

import dfs.slave.DFSSlave;

public class SlaveServiceImpl implements SlaveService{

    private DFSSlave slave;

    public SlaveServiceImpl(DFSSlave slave) {
        this.slave = slave;
    }

    @Override
    public byte[] readChunk(long chunkId, long offset, int size) {
        return slave.read(chunkId, offset, size);
    }

    @Override
    public boolean writeChunk(long chunkId, long offset, int size, byte[] data) {
        return slave.write(chunkId, offset, size, data);
    }

    @Override
    public boolean delete(long chunkId) {
        return slave.delete(chunkId);
    }
}

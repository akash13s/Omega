package service;

public interface SlaveService {

    public byte[] readChunk(long chunkId, long offset, int size);

    public boolean writeChunk(long chunkId, long offset, int size, byte[] data);

    public boolean delete(long chunkId);
}

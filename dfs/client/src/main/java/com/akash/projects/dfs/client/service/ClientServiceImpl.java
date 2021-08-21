package com.akash.projects.dfs.client.service;

import com.akash.projects.common.dfs.model.DfsChunk;
import com.akash.projects.common.dfs.model.DfsFile;
import com.akash.projects.common.dfs.model.DfsNode;
import com.akash.projects.dfs.master.service.MasterService;
import com.akash.projects.dfs.slave.service.SlaveService;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ClientServiceImpl implements ClientService {

    private MasterService masterService;

    public ClientServiceImpl(MasterService masterService) {
        this.masterService = masterService;
    }

    @Override
    public void loadFile(String localFilePath, int replicas, boolean isTextFile,
                        long blockSize, long lineCount) throws IOException {
        if (isTextFile) {
            writeTextFile(localFilePath, replicas, lineCount);
        }
        else {
           writeFile(localFilePath, replicas, blockSize);
        }
    }

    private void writeTextFile(String localFilePath, int replicas, long lineCount) throws IOException,
            RemoteException {
        File file = new File(localFilePath);
        DfsFile dfsFile = masterService.createFile(file.getName(), replicas);
        BufferedReader br = new BufferedReader(new FileReader(localFilePath));
        StringBuffer sb = new StringBuffer();
        long count = 0;
        long offset = 0;
        while (true) {
            String line = br.readLine();
            if (line!=null) {
                count++;
                sb.append(line).append("\n");
                if (count % lineCount == 0) {
                    DfsChunk dfsChunk = masterService.createChunk(dfsFile.getId(), offset, sb.length());
                    if (dfsChunk != null) {
                        byte[] data = sb.toString().getBytes();
                        writeChunk(data, dfsChunk);
                    }
                    offset += sb.length();
                    sb.delete(0, sb.length());
                }
            }
            else {
                br.close();
                break;
            }
        }
        if (sb.length()!=0) {
            DfsChunk dfsChunk = masterService.createChunk(dfsFile.getId(), offset, sb.length());
            if (dfsChunk != null) {
                byte[] data = sb.toString().getBytes();
                writeChunk(data, dfsChunk);
            }
        }
    }

    private void writeFile(String localFilePath, int replicas, long blockSize) throws IOException {
        FileInputStream fis = new FileInputStream(new File(localFilePath));
        DfsFile dfsFile = masterService.createFile(localFilePath.substring(
                localFilePath.lastIndexOf('/')), replicas);
        long offset = 0;
        while (true) {
            byte[] data = new byte[(int) blockSize];
            int sz = fis.read(data);
            if (sz>0) {
                // create chunk and write to chunk
                DfsChunk dfsChunk = masterService.createChunk(dfsFile.getId(), offset, (int) blockSize);
                if (dfsChunk!=null) {
                    writeChunk(data, dfsChunk);
                    offset += blockSize;
                }
            }
            else {
                fis.close();
                break;
            }
        }
    }

    private boolean writeChunk(byte[] data, DfsChunk dfsChunk) {
        List<DfsNode> nodes = dfsChunk.getNodes();
        for (DfsNode node: nodes) {
            try {
                Registry slaveRegistry = LocateRegistry.getRegistry(node.getRegistryHost(), node.getRegistryPort());
                SlaveService slaveService = (SlaveService) slaveRegistry.lookup(node.getServiceName());
                boolean write = slaveService.writeChunk(dfsChunk.getId(), 0, data.length, data);
                if (!write) {
                    return false;
                }
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public void getFile(String fileName) {

    }

    @Override
    public void listFiles() {

    }

    @Override
    public void deleteFile(String fileName) {

    }
}

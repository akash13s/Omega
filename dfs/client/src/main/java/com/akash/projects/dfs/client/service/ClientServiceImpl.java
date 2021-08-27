package com.akash.projects.dfs.client.service;

import com.akash.projects.common.dfs.model.DfsChunk;
import com.akash.projects.common.dfs.model.DfsFile;
import com.akash.projects.common.dfs.model.DfsNode;
import com.akash.projects.dfs.master.service.MasterService;
import com.akash.projects.common.dfs.service.SlaveService;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Objects;

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

    private SlaveService getSlaveService(DfsNode node) throws RemoteException, NotBoundException {
        Registry slaveRegistry = LocateRegistry.getRegistry(node.getRegistryHost(), node.getRegistryPort());
        return (SlaveService) slaveRegistry.lookup(node.getServiceName());
    }

    private boolean writeChunk(byte[] data, DfsChunk dfsChunk) {
        List<DfsNode> nodes = dfsChunk.getNodes();
        for (DfsNode node: nodes) {
            try {
                SlaveService slaveService = getSlaveService(node);
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
    public void getFile(String fileName) throws IOException, NotBoundException {
        DfsFile dfsFile = masterService.getFile(fileName);
        if (Objects.isNull(dfsFile)) {
            System.out.println("File not found!");
            return;
        }
        List<DfsChunk> dfsChunks = dfsFile.getChunks();
        dfsChunks.sort((c1, c2) -> (int) (c1.getOffset() - c2.getOffset()));
        boolean success = true;
        for (DfsChunk dfsChunk: dfsChunks) {
            int size = dfsChunk.getNodes().size();
            int failedAttempts = 0;
            for (DfsNode dfsNode: dfsChunk.getNodes()) {
                try {
                    SlaveService slaveService = getSlaveService(dfsNode);
                    byte[] data = slaveService.readChunk(dfsChunk.getId(), dfsChunk.getOffset(), dfsChunk.getSize());
                    String str;
                    if (data.length > 0) {
                        str = new String(data);
                        System.out.println(str);
                    }
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    failedAttempts++;
                    if (failedAttempts == size) {
                        System.out.println("Unable to retrieve file!");
                        success = false;
                    }
                }
            }
            if (!success) {
                break;
            }
        }
    }

    @Override
    public void listFiles() throws RemoteException {
        List<String> fileNames = masterService.listFiles();
        fileNames.forEach(System.out::println);
    }

    @Override
    public void deleteFile(String fileName) throws RemoteException {
        masterService.deleteFile(fileName);
        // call delete directly from master -> parallelize delete operation while deleting a chunk from various nodes
    }
}

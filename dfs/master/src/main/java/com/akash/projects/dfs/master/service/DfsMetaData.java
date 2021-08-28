package com.akash.projects.dfs.master.service;

import com.akash.projects.common.dfs.model.DfsChunk;
import com.akash.projects.common.dfs.model.DfsFile;
import com.akash.projects.common.dfs.model.DfsNode;
import com.akash.projects.dfs.master.constants.MasterConstants;
import com.akash.projects.dfs.master.constants.OperationType;
import com.akash.projects.dfs.master.model.EditOperation;
import com.akash.projects.dfs.master.utils.EditLogger;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DfsMetaData {

    // <registryHost:registryPort, nodeId>
    private static Map<String, Long> nodeIds;
    // <nodeId, dfsNode>
    private static Map<Long, DfsNode> nodeMap;
    // <fileId, filename>
    private static Map<Long, String> fileNameIdMap;
    // <fileName, dfsFile>
    private static Map<String, DfsFile> fileMap;
    // <chunkId, dfsChunk>
    private static Map<Long, DfsChunk> chunkMap;

    private ExecutorService executorService;

    private static EditLogger editLogger;

    public DfsMetaData(EditLogger editLogger) {
        nodeIds = new HashMap<>();
        nodeMap = new HashMap<>();
        fileMap = new HashMap<>();
        fileNameIdMap = new HashMap<>();
        chunkMap = new HashMap<>();
        executorService = Executors.newFixedThreadPool(MasterConstants.DEFAULT_THREAD_POOL_SIZE);
        DfsMetaData.editLogger = editLogger;
    }

    public DfsNode updateDfsNode(String registryHost, int registryPort, String serviceName, boolean writeLog)
            throws IOException {
        String key = registryHost + ":" + registryPort;
        DfsNode node;
        if (!nodeIds.containsKey(key)) {
            node = new DfsNode(DfsNode.counter.incrementAndGet(), registryHost, registryPort, serviceName, new Date());
            nodeIds.put(key, node.getId());
            nodeMap.put(node.getId(), node);
        }
        else {
            Long nodeId = nodeIds.get(key);
            node = nodeMap.get(nodeId);
            node.setLastActiveDate(new Date());
            nodeMap.put(nodeId, node);
        }
        if (writeLog) {
            dispatchLog(OperationType.UPDATE_NODE.getType(), new Object[] {registryHost, registryPort, serviceName});
        }
        return node;
    }

    public static void removeDfsNode(String registryHost, int registryPort, String serviceName, boolean writeLog) throws IOException {
        String key = registryHost + ":" + registryPort;
        if (nodeIds.containsKey(key)) {
            Long id = nodeIds.get(key);
            nodeIds.remove(key);
            nodeMap.remove(id);
            if (writeLog) {
                dispatchLog(OperationType.REMOVE_NODE.getType(), new Object[] {registryHost, registryPort, serviceName});
            }
        }
    }

    public DfsFile createFile(String fileName, int replicas, boolean writeLog) throws IOException {
        DfsFile dfsFile = new DfsFile(DfsFile.counter.incrementAndGet(), fileName, replicas);
        fileMap.put(fileName, dfsFile);
        fileNameIdMap.put(dfsFile.getId(), fileName);
        if (writeLog) {
            dispatchLog(OperationType.CREATE_FILE.getType(), new Object[] {fileName, replicas});
        }
        return dfsFile;
    }

    public void deleteFile(String fileName, boolean writeLog) throws IOException {
        DfsFile dfsFile = fileMap.get(fileName);
        List<DfsChunk> chunkList = dfsFile.getChunks();
        if (!chunkList.isEmpty()) {
            chunkList.forEach(chunk-> {
                executorService.execute(new RemoveChunkService(chunk));
            });
        }
        fileNameIdMap.remove(dfsFile.getId());
        fileMap.remove(fileName);
        if (writeLog) {
            dispatchLog(OperationType.DELETE_FILE.getType(), new Object[] {fileName});
        }
    }

    public DfsChunk createChunk(long fileId, long offset, int size, int actualSize, boolean writeLog) throws IOException {
        String fileName = fileNameIdMap.get(fileId);
        DfsFile dfsFile = fileMap.get(fileName);
        DfsChunk dfsChunk = null;
        if (Objects.nonNull(dfsFile)) {
            long chunkId = DfsChunk.counter.incrementAndGet();
            dfsChunk = new DfsChunk(chunkId, fileId, offset, size, actualSize);
            // allocate data nodes for the chunk
            List<DfsNode> nodes = allocateDataNodes(dfsFile.getReplicas());
            if (nodes.isEmpty()) {
                return null;
            }
            dfsChunk.setNodes(nodes);
            dfsFile.getChunks().add(dfsChunk);
            fileMap.put(fileName, dfsFile);
            chunkMap.put(chunkId, dfsChunk);
            if (writeLog) {
                dispatchLog(OperationType.CREATE_CHUNK.getType(), new Object[] {fileId, offset, size, actualSize});
            }
        }
        return dfsChunk;
    }

    private List<DfsNode> allocateDataNodes(int replicas) {
        List<DfsNode> nodes = new ArrayList<>(nodeMap.values());
        Collections.shuffle(nodes);
        List<DfsNode> allocatedNodes = new ArrayList<>();
        for (int i = 0; i<Math.min(replicas, nodes.size()); i++) {
            allocatedNodes.add(nodes.get(i));
        }
        return allocatedNodes;
    }

    public DfsFile getFile(String fileName) {
        return fileMap.get(fileName);
    }

    public List<String> listFiles() {
        List<String> fileNames = new ArrayList<>();
        fileMap.values().forEach(file->fileNames.add(file.getFileName()));
        return fileNames;
    }

    public static Map<Long, DfsNode> getNodeMap() {
        return nodeMap;
    }

    private static void dispatchLog(String operation, Object[] arguments) throws IOException {
        EditOperation editOperation = new EditOperation(Date.from(Instant.now()).toString(), operation, arguments);
        editLogger.addOperation(editOperation);
    }
}

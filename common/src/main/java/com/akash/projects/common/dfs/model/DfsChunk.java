package com.akash.projects.common.dfs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DfsChunk implements Serializable {

    private long id;
    private long fileId;
    private long offset;
    private int size;
    private int actualSize;
    private Date createdDate;
    private List<DfsNode> nodes;

    public static AtomicLong counter = new AtomicLong(0);

    public DfsChunk(long id, long fileId, long offset, int size, int actualSize) {
        this.id = id;
        this.fileId = fileId;
        this.offset = offset;
        this.size = size;
        this.actualSize = actualSize;
        this.createdDate = new Date();
        this.nodes = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public List<DfsNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<DfsNode> nodes) {
        this.nodes = nodes;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getActualSize() {
        return actualSize;
    }

    public void setActualSize(int actualSize) {
        this.actualSize = actualSize;
    }
}

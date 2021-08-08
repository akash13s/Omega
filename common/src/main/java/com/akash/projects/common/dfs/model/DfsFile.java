package com.akash.projects.common.dfs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public class DfsFile implements Serializable {

    private long id;
    private String fileName;
    private int replicas;
    private List<DfsChunk> chunks;
    private Date createdDate;

    public static AtomicLong counter = new AtomicLong(0);

    public DfsFile(long id, String fileName, int replicas) {
        this.id = id;
        this.fileName = fileName;
        this.replicas = replicas;
        this.createdDate = new Date();
        this.chunks = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getReplicas() {
        return replicas;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
    }

    public List<DfsChunk> getChunks() {
        return chunks;
    }

    public void setChunks(List<DfsChunk> chunks) {
        this.chunks = chunks;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}

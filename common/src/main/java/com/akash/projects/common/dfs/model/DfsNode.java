package com.akash.projects.common.dfs.model;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class DfsNode implements Serializable {

    private long id;
    private String registryHost;
    private int registryPort;
    private String serviceName;
    private Date lastActiveDate;

    public static AtomicLong counter = new AtomicLong(0);

    public DfsNode() {
    }

    public DfsNode(long id, String registryHost, int registryPort, String serviceName, Date lastActiveDate) {
        this.id = id;
        this.registryHost = registryHost;
        this.registryPort = registryPort;
        this.serviceName = serviceName;
        this.lastActiveDate = lastActiveDate;
    }

    public long getId() {
        return id;
    }


    public String getRegistryHost() {
        return registryHost;
    }

    public void setRegistryHost(String registryHost) {
        this.registryHost = registryHost;
    }

    public int getRegistryPort() {
        return registryPort;
    }

    public void setRegistryPort(int registryPort) {
        this.registryPort = registryPort;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Date getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(Date lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }
}

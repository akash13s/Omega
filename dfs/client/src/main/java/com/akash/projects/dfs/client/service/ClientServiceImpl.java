package com.akash.projects.dfs.client.service;

import com.akash.projects.dfs.master.service.MasterService;

public class ClientServiceImpl implements ClientService {

    private MasterService masterService;

    public ClientServiceImpl(MasterService masterService) {
        this.masterService = masterService;
    }

    @Override
    public void putFile(String localFilePath, int replicas, boolean isTextFile) {

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

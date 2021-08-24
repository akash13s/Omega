package com.akash.projects.dfs.client;

import com.akash.projects.common.dfs.constants.MasterRegistryInfo;
import com.akash.projects.dfs.client.constants.ClientConstants;
import com.akash.projects.dfs.client.service.ClientService;
import com.akash.projects.dfs.client.service.ClientServiceImpl;
import com.akash.projects.dfs.master.service.MasterService;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DFSClient {

    @Parameter(names = {"--master-registry-host", "-mrh"}, description = "Master registry host")
    private String masterRegistryHost = MasterRegistryInfo.DEFAULT_REGISTRY_HOST;

    @Parameter(names = {"--master-registry-port", "-mrp"}, description = "Master registry port")
    private int masterRegistryPort = MasterRegistryInfo.DEFAULT_REGISTRY_PORT;

    private ClientService clientService;

    private MasterService masterService;

    private void start() throws RemoteException, NotBoundException {
        Registry masterRegistry = LocateRegistry.getRegistry(masterRegistryHost, masterRegistryPort);
        masterService = (MasterService) masterRegistry.lookup(MasterService.class.getCanonicalName());
        clientService = new ClientServiceImpl(masterService);
    }

    private void handleUserOperations() throws IOException, NotBoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.println("Press 1 to load file, 2 to get file, 3 to list files, 4 to  delete file, 5 to  exit");
            Integer op = Integer.parseInt(br.readLine());
            switch(op) {
                case 1:
                    handleLoadFile(br);
                    break;
                case 2:
                    handleGetFile(br);
                    break;
                case 3:
                    handleListFiles();
                    break;
                case 4:
                    handleDeleteFile(br);
                    break;
                case 5:
                    br.close();
                    System.exit(0);
                default:
                    System.out.println("Please enter valid option");
            }
        }
    }

    private void handleLoadFile(BufferedReader br) throws IOException {
        System.out.println("Enter local file path which you want to upload");
        String localFilePath = br.readLine();
        System.out.println("Enter number of replicas");
        int replicas = Integer.parseInt(br.readLine());
        System.out.println("Is the file text/non-text? Enter : yes/no");
        boolean isTextFile = br.readLine().equalsIgnoreCase("yes");
        clientService.loadFile(localFilePath, replicas, isTextFile,
                ClientConstants.DEFAULT_BLOCK_SIZE, ClientConstants.DEFAULT_LINE_COUNT);
    }

    private void handleGetFile(BufferedReader br) throws IOException, NotBoundException {
        System.out.println("Enter the name of file which you want to retrieve");
        String fileName = br.readLine();
        clientService.getFile(fileName);
    }

    private void handleListFiles() throws RemoteException {
        clientService.listFiles();
    }

    private void handleDeleteFile(BufferedReader br) throws IOException {
        System.out.println("Enter the name of file which you want to delete");
        String fileName = br.readLine();
        clientService.deleteFile(fileName);
    }

    public static void main(String[] args) {
        DFSClient client = new DFSClient();
        JCommander commander = JCommander.newBuilder()
                .addObject(client)
                .build();
        commander.parse(args);

        try {
            client.start();
            System.out.println("Client started");
            client.handleUserOperations();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

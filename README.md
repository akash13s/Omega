# Omega - A Distributed File System

`The design concepts of this DFS largely come from the Google File System.` 
<br> Read about [Google File System](http://research.google.com/archive/gfs-sosp2003.pdf) here.

It has 3 components:
* `Master` - This is the main controller node that the client initially connects to. It is responsible for maintaining the meta-data of files.
* `Slave` - These are the storage nodes that are controlled by the master node. Data is stored and replicated on these nodes.
* `Client` - The program through which the user uploads/lists files.

<img width="700" alt="DFS" src="https://user-images.githubusercontent.com/33151745/131530857-41e3386a-a544-461d-9f4e-b29b115ba5e6.png">

## Features

- Allows client to load, fetch, delete, and list files
- Communication framework used is [RMI](https://en.wikipedia.org/wiki/Java_remote_method_invocation)  
- Design can be scaled for any number of slaves
- Heartbeat check of workers at regular intervals
- Recovery of master meta-data from logs, in case the master goes down

## How to start the application

### Prerequisites
- `Java 8 and Maven should be set up`

### Steps

`Step 1: For running DFSMaster.java, add the below VM arguments accordingly and run `<br>
   --master-registry-host <eg: localhost> <br>
   --master-registry-port <eg: 8085> <br>
   --logPath <eg: /Users/akashshrivastva/Desktop/dfs-master/log/master.log>
   
`Step 2: Run DFSSlave.java, add the below VM arguments accordingly and run`<br>
   --master-registry-host <eg: localhost> <br>
   --master-registry-port <eg: 8085> <br>
   --registry-port <eg: 8081> [NOTE: This is the slave registry port. In case you are running both the master
and slave on same machine, this port number needs to be different from that of master's.]<br>
   --service-name <eg: worker-1> [NOTE: If you are running multiple slaves on the same machine, give each of them
a different service name] <br>
   --slave-data-path <eg: /Users/akashshrivastva/Desktop/dfs>

   
`Step 3: Run DFSClient.java, add the below VM arguments accordingly and run`<br>
   --master-registry-host <eg: localhost> <br>
   --master-registry-port <eg: 8085> <br>
   

## Omega - A Distributed File System

``The design concepts of this DFS largely come from the Google File System.``

It has 3 components:
* ``Master`` - This is the main controller node that the client initially connects to. It is responsible for maintaining the meta-data of files.
* ``Slave`` - These are the storage nodes that are controlled by the master node. Data is stored and replicated on these nodes.
* ``Client`` - The program through which the user uploads/lists files.

<img width="700" alt="DFS" src="https://user-images.githubusercontent.com/33151745/131530857-41e3386a-a544-461d-9f4e-b29b115ba5e6.png">

### Features
----

- Allows client to load, fetch, delete, and list files
- Design can be scaled for any number of slaves
- Heartbeat check of workers at regular intervals
- Recovery of master meta-data from logs, in case the master goes down

### How to run the application
-----



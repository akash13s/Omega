import com.akash.projects.common.dfs.model.DfsFile;
import com.akash.projects.common.dfs.model.DfsNode;
import com.akash.projects.dfs.master.service.DfsMetaData;
import com.akash.projects.dfs.master.service.MasterServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class MasterServiceImplTest {

    private MasterServiceImpl masterService;

    @Before
    public void init() throws RemoteException {
        masterService = new MasterServiceImpl();
    }

    @Test
    public void testCreateFile() throws RemoteException {
        masterService.createFile("hello.txt", 3);
        DfsFile file = masterService.getFile("hello.txt");
        Assert.assertNotNull(file);
        Assert.assertEquals("hello.txt", file.getFileName());
    }

    @Test
    public void testDeleteFile() throws RemoteException {
        masterService.createFile("hello.txt", 3);
        DfsFile file = masterService.getFile("hello.txt");
        Assert.assertNotNull(file);
        masterService.deleteFile(file.getId());
        DfsFile newFile = masterService.getFile("file1.txt");
        Assert.assertNull(newFile);
    }

    @Ignore
    @Test
    public void testCreateChunk() throws RemoteException {
        masterService.createFile("hello.txt", 3);
        DfsFile file = masterService.getFile("hello.txt");
        masterService.createChunk(file.getId(), 0, 10);
        masterService.createChunk(file.getId(), 10, 10);
        DfsFile newFile = masterService.getFile("hello.txt");
        Assert.assertEquals(0, newFile.getChunks().size());
    }

    @Test
    public void testListFiles() throws RemoteException {
        masterService.createFile("file1.txt", 3);
        masterService.createFile("file2.txt", 2);
        List<DfsFile> files = masterService.listFiles();
        Assert.assertEquals(2, files.size());
    }

//    @Test
//    public void testAddDfsNode() throws RemoteException {
//        DfsNode node = masterService.addDfsNode("192.168.0.102", 8076, "worker-1");
//        Assert.assertNotNull(node);
//        Map<Long, DfsNode> nodeMap = DfsMetaData.getNodeMap();
//        Assert.assertEquals("worker-1", nodeMap.get(node.getId()).getServiceName());
//    }
}

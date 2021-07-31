import com.akash.projects.dfs.slave.DFSSlave;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TestDFSSlave {

    private DFSSlave slave;

    @Before
    public void init() {
        slave = new DFSSlave();
    }

    @Test
    public void testReadChunk() throws IOException {
        byte[] data = slave.read(0, 0, 12, true);
        String str = new String(data);
        Assert.assertEquals(str, "Hello world!");
    }

    @Test
    public void testWriteChunk() throws IOException {
        byte[] data = slave.read(0, 0, 12, true);
        boolean ret = slave.write(1, 0, 12, data, true);
        Assert.assertTrue(ret);

        byte[] data1 = slave.read(1, 0, 12, true);
        String str1 = new String(data1);
        Assert.assertEquals(str1, "Hello world!");
    }

    @Test
    public void testDeleteChunk() throws IOException {
        byte[] data = slave.read(0, 0, 12, true);
        boolean ret = slave.write(2, 0, 12, data, true);
        Assert.assertTrue(ret);

        byte[] data1 = slave.read(2, 0, 12, true);
        String str1 = new String(data1);
        Assert.assertEquals(str1, "Hello world!");

        boolean ret1 = slave.delete(2, true);
    }

}

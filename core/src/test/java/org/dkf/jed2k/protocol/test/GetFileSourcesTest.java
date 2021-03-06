package org.dkf.jed2k.protocol.test;

import org.dkf.jed2k.exception.JED2KException;
import org.dkf.jed2k.protocol.Hash;
import org.dkf.jed2k.protocol.server.GetFileSources;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static junit.framework.Assert.assertEquals;

public class GetFileSourcesTest {

    @Test
    public void testPutGet() throws JED2KException {
        ByteBuffer bb = ByteBuffer.allocate(28);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        GetFileSources gfs = new GetFileSources();
        gfs.hash = Hash.EMULE;
        gfs.lowPart = 10;
        gfs.hiPart  = 5;
        gfs.put(bb);
        assertEquals(0, bb.remaining());
        bb.flip();
        GetFileSources gfs_in = new GetFileSources();
        gfs_in.get(bb);
        assertEquals(Hash.EMULE, gfs_in.hash);
        assertEquals(10, gfs_in.lowPart);
        assertEquals(5, gfs_in.hiPart);
    }
}

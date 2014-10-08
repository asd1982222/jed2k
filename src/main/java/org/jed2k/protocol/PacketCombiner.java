package org.jed2k.protocol;

import java.util.Map;
import java.util.TreeMap;

public class PacketCombiner {
    
    enum ProtocolType {
        OP_EDONKEYHEADER(0xE3), 
        OP_EDONKEYPROT(0xE3), 
        OP_PACKEDPROT(0xD4), 
        OP_EMULEPROT(0xC5);

        public final byte value;

        ProtocolType(int v) {
            this.value = (byte)v;
        }
    }
    
    enum ClientServerTcp {
        OP_LOGINREQUEST(0x01), // <HASH 16><ID 4><PORT 2><1 Tag_set>
        OP_REJECT(0x05), // (null)
        OP_GETSERVERLIST(0x14), // (null)client->server
        OP_OFFERFILES(0x15), // <count 4>(<HASH 16><ID 4><PORT 2><1
                             // Tag_set>)[count]
        OP_SEARCHREQUEST(0x16), // <Query_Tree>
        OP_DISCONNECT(0x18), // (not verified)
        OP_GETSOURCES(0x19), // <HASH 16>
                             // v2 <HASH 16><SIZE_4> (17.3) (mandatory on 17.8)
                             // v2large <HASH 16><FILESIZE 4(0)><FILESIZE 8>
                             // (17.9) (large files only)
        OP_SEARCH_USER(0x1A), // <Query_Tree>
        OP_CALLBACKREQUEST(0x1C), // <ID 4>
        // OP_QUERY_CHATS = 0x1D, // (deprecated, not supported by server any
        // longer)
        // OP_CHAT_MESSAGE = 0x1E, // (deprecated, not supported by server any
        // longer)
        // OP_JOIN_ROOM = 0x1F, // (deprecated, not supported by server any
        // longer)
        OP_QUERY_MORE_RESULT(0x21), // ?
        OP_GETSOURCES_OBFU(0x23), OP_SERVERLIST(0x32), // <count 1>(<IP 4><PORT
                                                       // 2>)[count]
                                                       // server->client
        OP_SEARCHRESULT(0x33), // <count 4>(<HASH 16><ID 4><PORT 2><1
                               // Tag_set>)[count]
        OP_SERVERSTATUS(0x34), // <USER 4><FILES 4>
        OP_CALLBACKREQUESTED(0x35), // <IP 4><PORT 2>
        OP_CALLBACK_FAIL(0x36), // (null notverified)
        OP_SERVERMESSAGE(0x38), // <len 2><Message len>
        // OP_CHAT_ROOM_REQUEST = 0x39, // (deprecated, not supported by server
        // any longer)
        // OP_CHAT_BROADCAST = 0x3A, // (deprecated, not supported by server any
        // longer)
        // OP_CHAT_USER_JOIN = 0x3B, // (deprecated, not supported by server any
        // longer)
        // OP_CHAT_USER_LEAVE = 0x3C, // (deprecated, not supported by server
        // any longer)
        // OP_CHAT_USER = 0x3D, // (deprecated, not supported by server any
        // longer)
        OP_IDCHANGE(0x40), // <NEW_ID 4>
        OP_SERVERIDENT(0x41), // <HASH 16><IP 4><PORT 2>{1 TAG_SET}
        OP_FOUNDSOURCES(0x42), // <HASH 16><count 1>(<ID 4><PORT 2>)[count]
        OP_USERS_LIST(0x43), // <count 4>(<HASH 16><ID 4><PORT 2><1
                             // Tag_set>)[count]
        OP_FOUNDSOURCES_OBFU(0x44); // <HASH 16><count 1>(<ID 4><PORT 2><obf
                                    // settings 1>(UserHash16 if
                                    // obf&0x08))[count]

        public final byte value;

        ClientServerTcp(int v) {
            value = (byte)v;
        }
    }
    
    private PacketHeader header = new PacketHeader();
    private static final Map<PacketKey, Class<? extends Serializable>> supportedPackets;
    
    private static void addHandler(byte protocol, byte type, Class<? extends Serializable> clazz) {
        PacketKey pk = new PacketKey(protocol, type);
        assert(!supportedPackets.containsKey(pk));
        assert(clazz != null);
        supportedPackets.put(pk, clazz);
    }
    
    static {
        supportedPackets = new TreeMap<PacketKey, Class<? extends Serializable>>();
        addHandler(ProtocolType.OP_EDONKEYHEADER.value, ClientServerTcp.OP_LOGINREQUEST.value, LoginRequest.class);
        addHandler(ProtocolType.OP_EDONKEYHEADER.value, ClientServerTcp.OP_SERVERLIST.value, ServerList.class);        
        
    }
    
    public Serializable combine(Buffer src) throws ProtocolException {
        if (!header.isDefined()) {
            if (src.remaining() >= header.size()) {
                header.get(src);
            } else {
                return null;
            }
        }
        
        if (src.remaining() >= header.size) {
            PacketKey key = header.key();
            Class<? extends Serializable> clazz = supportedPackets.get(key);
            Serializable ph = null;
            
            if (clazz != null) {
                try {
                    ph = clazz.newInstance();
                } catch(InstantiationException e) {
                    throw new ProtocolException(e);
                } catch (IllegalAccessException e) {
                    throw new ProtocolException(e);                    
                }
            } else {
                ph = new BytesSkipper(header.size);
            }
            
            ph.get(src);
            return ph;
        }
        
        return null;
    }
}
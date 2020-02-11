package datafiles;

import domain.Member;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author xbp1
 */
public class MembersFile {
    
    private final RandomAccessFile raf;
    
    public MembersFile(String fname) throws IOException {
        raf = new RandomAccessFile(fname, "rw");
    }
    
    public void clear() throws IOException {
        raf.setLength(0);
    }
    
    public Member readMember(long id) throws IOException {
        byte[] record = new byte[Member.SIZE];
        raf.seek((id - 1) * Member.SIZE);
        raf.read(record);
        return Member.fromBytes(record);
    }
    
    public void saveMember(Member member) throws IOException {
        byte[] record = member.toBytes();
        raf.seek((member.getId() - 1) * Member.SIZE);
        raf.write(record);
    }
    
    public long numMembers() throws IOException {
        return raf.length()/Member.SIZE;
    }
    
    public void close() throws IOException {
        raf.close();
    }   
}
package domain;

import utils.PackUtils;

public class Book {
    
    public static final int TITLE_LIMIT = 20;
    
    public static final int SIZE = 8 + 2*TITLE_LIMIT + 8; // Paso 1
    
    private final long id;
    private final String title;
    private long idMember;

    public Book(long id, String title) {
        this.id = id;
        this.title = title;
        this.idMember = -1L;
    }
        
    private Book(long id, String title, long idSoci) {
        this.id = id;
        this.title = title;
        this.idMember = idSoci;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getIdMember() {
        return idMember;
    }

    public void setIdMember(long idMember) {
        this.idMember = idMember;
    }
    
    public static Book fromBytes(byte[] record) {
        int offset = 0;
        long id = PackUtils.unpackLong(record, offset);
        offset += 8;
        String title = PackUtils.unpackLimitedString(TITLE_LIMIT, record, offset);
        offset += 2 * TITLE_LIMIT;
        long idMember = PackUtils.unpackLong(record, offset);
        return new Book(id, title, idMember);
    }
    
    public byte[] toBytes() {
        byte[] record = new byte[SIZE];
        int offset = 0;
        PackUtils.packLong(id, record, offset);
        offset += 8;
        PackUtils.packLimitedString(title, TITLE_LIMIT, record, offset);
        offset += 2 * TITLE_LIMIT;
        PackUtils.packLong(idMember, record, offset);
        return record;
    }
}
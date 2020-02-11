package domain;

import utils.PackUtils;

public class Member {
    
    private static final int MAX_BOOKS = 3;
    
    public static final int NAME_LIMIT = 20;
    public static final int ADDRESS_LIMIT = 30;
    
    public static final int SIZE = 8 + 2 * NAME_LIMIT + 2 * ADDRESS_LIMIT + 8 * MAX_BOOKS; // Paso 2
    
    private final long id;
    private final String name;
    private final String address;
    private final long[] idBooks;

    public Member(long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.idBooks = new long[] {-1L, -1L, -1L};
    }
    
    private Member(long id, String name, String address, long[] idBooks) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.idBooks = idBooks;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int countBooks() {
        int count = 0;
        for(int i=0; i<3; ++i) {
            if(idBooks[i] != -1L) {
                ++count;
            }
        }
        return count;
    }

    public boolean canBorrow() {
        return countBooks() < MAX_BOOKS;
    }
        
    public void addBook(long idBook) {
        if(canBorrow()) {
            for(int i=0; i<3; ++i) {
                if(idBooks[i] == -1L && !containsBook(idBook)) {
                    idBooks[i] = idBook;
                }
            }
        }
    }

    public void removeBook(long idBook) {
        for(int i=0; i<3; ++i) {
            if(idBooks[i] == idBook) {
                idBooks[i] = -1L;
            }
        }
    }

    public boolean containsBook(long idBook) {
        for(int i=0; i<3; ++i) {
            if(idBooks[i] == idBook) {
                return true;
            }
        }
        return false;
    }   
    
    public static Member fromBytes(byte[] record) {
        int offset = 0;
        long id = PackUtils.unpackLong(record, offset);
        offset += 8;
        String name = PackUtils.unpackLimitedString(NAME_LIMIT, record, offset);
        offset += 2 * NAME_LIMIT;
        String address = PackUtils.unpackLimitedString(ADDRESS_LIMIT, record, offset);
        offset += 2 * ADDRESS_LIMIT;
        long[] idBooks = new long [3];
        for(int i=0; i<3; ++i) {
            idBooks[i] = PackUtils.unpackLong(record, offset);
            offset += 8;
        }
        return new Member(id, name, address, idBooks);
    }
    
    public byte[] toBytes() {
        byte[] record = new byte[SIZE];
        int offset = 0;
        PackUtils.packLong(id, record, offset);
        offset += 8;
        PackUtils.packLimitedString(name, NAME_LIMIT, record, offset);
        offset += 2 * NAME_LIMIT;
        PackUtils.packLimitedString(address, ADDRESS_LIMIT, record, offset);
        offset += 2 * ADDRESS_LIMIT;
        for(int i=0; i<3; ++i) {
            PackUtils.packLong(idBooks[i], record, offset);
            offset += 8;
        }
        return record;
    }
}

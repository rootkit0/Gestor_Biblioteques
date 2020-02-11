package datafiles;

import domain.Book;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author xbp1
 */
public class BooksFile {
    
    private final RandomAccessFile raf;
    
    public BooksFile(String fname) throws IOException {
        raf = new RandomAccessFile(fname, "rw");
    }
    
    public void clear() throws IOException {
        raf.setLength(0);
    }
    
    public Book read(long id) throws IOException {
        byte[] record = new byte[Book.SIZE];
        raf.seek((id - 1) * Book.SIZE);
        raf.read(record);
        return Book.fromBytes(record);
    }
    
    public void write(Book book) throws IOException {
        byte[] record = book.toBytes();
        raf.seek((book.getId() - 1) * Book.SIZE);
        raf.write(record);
    }
    
    public long length() throws IOException {
        return raf.length()/Book.SIZE;
    }
    
    public void close() throws IOException {
        raf.close();
    }
}
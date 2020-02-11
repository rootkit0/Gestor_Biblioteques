package librarymanager;

import acm.program.CommandLineProgram;
import domain.Book;
import domain.Member;
import datafiles.BooksFile;
import datafiles.MembersFile;
import debug.Inspector;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class LibraryManager extends CommandLineProgram {

    private static final String BOOKS     = "llibres.dat";
    private static final String MEMBERS   = "socis.dat";
    private static final String LOG       = "manager.log";
    private static final String MOVEMENTS = "movements.csv";
    
    private BufferedReader  movements;
    private BufferedWriter  logFile;
    
    private BooksFile   booksFile;
    private MembersFile membersFile;
    
    private int movementNumber;
    
    // Entry points
    
    public static void main(String[] args) {
        new LibraryManager().start(args);
    } 
    
    public void run() {
        try {
            openFiles();
            clearFiles();
            processMovements();
            closeFiles();
            debug();
        } catch (IOException ex) {
            println("ERROR something about the files");
        }
    }

    // Dump books and members on the console
    
    private void debug() {
        println("--- BEGIN BOOKS ---");
        print(Inspector.inspectBooks(BOOKS));
        println("--- END BOOKS ---");
        println("--- BEGIN MEMBERS ---");
        print(Inspector.inspectMembers(MEMBERS));
        println("--- END MEMBERS ---");
    }
    
    private void clearFiles() throws IOException {
        booksFile.clear();
        membersFile.clear();
    }
    
    // Opening and closing
    
    private void openFiles() throws IOException {
        booksFile = new BooksFile(BOOKS);
        membersFile = new MembersFile(MEMBERS);
        movements = new BufferedReader(new FileReader(MOVEMENTS));
        logFile = new BufferedWriter(new FileWriter(LOG));
    }
    
    private void closeFiles() throws IOException {
        booksFile.close();
        membersFile.close();
        movements.close();
        logFile.close();
    }

    // Movements processing
    
    private void processMovements() throws IOException {
        String line = movements.readLine();
        while (line != null) {
            ++movementNumber;
            processMovements(line);
            line = movements.readLine();
        }
    }
    
    private void processMovements(String line) throws IOException {
        StringTokenizer tokenizer = new StringTokenizer(line, ",");
        String operacio = tokenizer.nextToken();
        if(operacio.equals("ALTALIBRO")) {
            String titol = tokenizer.nextToken();
            processAltaLlibre(titol);
        }
        if (operacio.equals("ALTASOCIO")) {
            String nom = tokenizer.nextToken();
            String direccio = tokenizer.nextToken();
            processAltaSoci(nom, direccio);
        }
        if (operacio.equals("PRÉSTAMO")) {
            long idLlibre = Long.parseLong(tokenizer.nextToken());
            long idSoci = Long.parseLong(tokenizer.nextToken());
            processPrestec(idLlibre, idSoci);
        }
        if (operacio.equals("DEVOLUCIÓN")) {
            long idLlibre = Long.parseLong(tokenizer.nextToken());
            processDevolucio(idLlibre);
        }
    }
    
    private void processAltaLlibre(String titol) throws IOException {
        long idNouLlibre = booksFile.length() + 1;
        Book llibre = new Book(idNouLlibre, titol);
        booksFile.write(llibre);
        logFile.write(movementNumber + ": OK alta de libro con identificador " + idNouLlibre + "\n");
    }
    
    private void processAltaSoci(String nom, String direccio) throws IOException {
        long idNouMembre = membersFile.numMembers() + 1;
        Member membre = new Member(idNouMembre, nom, direccio);
        membersFile.saveMember(membre);
        logFile.write(movementNumber + ": OK alta de socio con identificador " + idNouMembre + "\n");
    }
    
    private void processPrestec(long idLlibre, long idSoci) throws IOException {
        if(idLlibre > 0 && idLlibre <= booksFile.length()) {
            if(idSoci > 0 && idSoci <= membersFile.numMembers()) {
                if(booksFile.read(idLlibre).getIdMember() == -1L) {
                    if(membersFile.readMember(idSoci).canBorrow()) {
                        
                        Book llibre = booksFile.read(idLlibre);
                        llibre.setIdMember(idSoci);
                        booksFile.write(llibre);
                        
                        Member membre = membersFile.readMember(idSoci);
                        membre.addBook(idLlibre);
                        membersFile.saveMember(membre);
                        
                        logFile.write(movementNumber + ": OK préstamo del libro con código " + idLlibre + " realizado con éxito al socio con identificador " + idSoci + "\n");
                    }
                    else {
                        logFile.write(movementNumber + ": ERROR préstamo: el socio con identificador " + idSoci + " ya tiene el máximo de libros permitidos\n");
                    }
                }
                else {
                    logFile.write(movementNumber + ": ERROR préstamo: el libro con código " + idLlibre + " ya esta prestado\n");
                }
            }
            else {
                logFile.write(movementNumber + ": ERROR préstamo: no existe el socio con identificador " + idSoci + "\n");
            }
        }
        else {
            logFile.write(movementNumber + ": ERROR préstamo: no existe el libro con código " + idLlibre + "\n");
        }
    }
    
    private void processDevolucio(long idLlibre) throws IOException {
        if(idLlibre > 0 && idLlibre <= booksFile.length()) {
            if(booksFile.read(idLlibre).getIdMember() != -1L) {
                long idSoci = booksFile.read(idLlibre).getIdMember();
                if(idSoci > 0 && idSoci <= membersFile.numMembers()) {
                    if(membersFile.readMember(idSoci).containsBook(idLlibre)) {
                        
                        Book llibre = booksFile.read(idLlibre);
                        llibre.setIdMember(-1L);
                        booksFile.write(llibre);
                        
                        Member membre = membersFile.readMember(idSoci);
                        membre.removeBook(idLlibre);
                        membersFile.saveMember(membre);
                        
                        logFile.write(movementNumber + ": OK devolución del libro con código " + idLlibre + " realizada con éxito por el socio con identificador " + idSoci + "\n");
                    }
                    else {
                       logFile.write(movementNumber + ": ERROR devolución: el socio con identificador " + idSoci + " no tiene en préstamo el libro con código " + idLlibre + "\n"); 
                    }
                }
                else {
                    logFile.write(movementNumber + ": ERROR devolución: no existe el socio con identificador " + idSoci + "\n");
                }
            }
            else {
                logFile.write(movementNumber + ": ERROR devolución: el libro con código " + idLlibre + " no está prestado\n");
            }
        }
        else {
            logFile.write(movementNumber + ": ERROR devolución: no existe el libro con código " + idLlibre + "\n");
        }
    }
}
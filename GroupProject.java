import java.util.*;

class Book {
    private String title; // book title
    private String author; // book author
    private String publication; // company of publication
    private String synopsis; //
    private String isbn;
    private int edition;

    public Book(String title, String author, String publication, String synopsis, String isbn, int edition) {
        this.title = title;
        this.author = author;
        this.publication = publication;
        this.synopsis = synopsis;
        this.isbn = isbn;
        this.edition = edition;
        this.version = EDITION + edition;
    }

    public String getTitle() {
        return title;
    }

    public String getAuhor() {
        return author;
    }

    public String getPublication() {
        return publication;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getVersion() {
        return version;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getEdition() {
        return edition;
    }

}

class LibrarySystem {
    private Book[] booksByISBN;
    private Book[] booksByTitles;
    private Book[] booksByAuthors;
    private Queue<Book> borrowedBooks;
    private int libraryCapacity;
    private int booksInTheLibrary;
    private int borrowed;
    private static final int DEFAULT_CAPACITY = 41;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    private static final int RESIZE = 2;

    public LibrarySystem() {
        this(DEFAULT_CAPACITY);
    }

    public LibrarySystem(int libraryCapacity) {
        this.libraryCapacity = libraryCapacity;
        this.booksByISBN = new Book[libraryCapacity];
        this.booksByTitles = new Book[libraryCapacity];
        this.booksByAuthors = new Book[libraryCapacity];
        this.borrowedBooks = new LinkedList<>();
        this.booksInTheLibrary = 0;

    }

    // Return the capacity of the table.
    public int libraryStorageCapacity() {
        return booksByISBN.length;
    }

    // Return the number of elements in the table.
    public int booksInTheLibrary() {
        return booksInTheLibrary;
    }

    // Checks if the table is empty.
    public boolean isLibraryEmpty() {
        if (booksInTheLibrary == 0) {
            return true;
        } else {
            return false;
        }
    }

    // Add Student data entry into the hash table
    public boolean addBookData(Book book) {

        if (loadFactor() >= LOAD_FACTOR_THRESHOLD) {
            resizeTheTable();
        }

        int key1 = book.getIsbn().hashCode();
        int key2 = book.getAuhor().hashCode();
        int key3 = book.getTitle().hashCode();

        int index1 = primaryHash(key1);
        int index2 = primaryHash(key2);
        int index3 = primaryHash(key3);

        int addition1 = addition2 = addition3 = 0;

        while (booksByISBN[index1] != null) {
            index = doubleHashing(key1, addition1);
            addition1++;
        }

        while (booksByTitles[index2] != null) {
            index = doubleHashing(key2, addition2);
            addition2++;
        }

        while (booksByAuthors[index3] != null) {
            index = doubleHashing(key3, addition3);
            addition3++;
        }

        booksByISBN[index1] = book;
        booksByTitles[index2] = book;
        booksByAuthors[index3] = book;
        booksInTheLibrary++;

        return true;
    }

    // Delete boo data entry from the hash table.
    public boolean deleteBookData(Book book) {
        int key = book.numericValue();
        int index = primaryHash(key);
        int deletion = 0;

        while (libraryEntries[index] != null) {
            if (libraryEntries[index].getKey() == key) {
                enqueue(book);
                libraryEntries[index] = null;
                booksInTheLibrary--;
                return true;
            }
            index = doubleHashing(key, deletion);
            deletion++;
        }
        return false;
    }

    // Retrieve the grade (value) in the hash table.
    public Book searchBook(Book book) {
        int key = book.numericValue();
        int primaryIndex = primaryHash(key);
        int secondaryIndex = secondaryHash(key);
        int retrieve = 0;

        while (retrieve < libraryCapacity) {
            int index = (primaryIndex + retrieve * secondaryIndex) % libraryCapacity;

            if (libraryEntries[index] == null) {
                System.out.println("Key not found!!");
                return null;
            }

            else if (libraryEntries[index].getKey() == key) {
                return libraryEntries[index];
            }
            retrieve++;
        }
        System.out.println("Key not found!!");
        return null;
    }

    // Add a book to the end of the queue.
    private void enqueue(Book book) {
        borrowedBooks.add(book);
        borrowed++;
    }

    // Primary hash function.
    private int primaryHash(int primary) {
        return (primary % libraryStorageCapacity());
    }

    // Secondary hash function for double hashing.
    private int secondaryHash(int secondary) {
        int prime = 0;

        for (int s = 0; s < booksInTheLibrary(); s++) {

            if (isPrime((booksInTheLibrary() - s))) {
                prime = (booksInTheLibrary() - s);
            }
        }
        return (prime - (secondary % prime));
    }

    // Double hashing for collision resolution.
    private int doubleHashing(int resolution, int s) {
        return ((primaryHash(resolution) + (s * secondaryHash(resolution))) % libraryStorageCapacity());
    }

    // Checks if a number is prime.
    private boolean isPrime(int number) {

        if (number <= 1) {
            return false;
        }

        for (int n = 2; n <= Math.sqrt(number); n++) {
            if (number % n == 0) {
                return false;
            }
        }
        return true;
    }

    // Calculates the load factor of the hash table.
    private double loadFactor() {
        return (double) (booksInTheLibrary() / libraryStorageCapacity());
    }

    // Gets the next prime number for resizing
    private int getNextPrime(int number) {
        while (!isPrime(number)) {
            number++;
        }
        return number;
    }

    // Resizes the hash table and rehash entries.
    private void resizeTheTable() {
        int newCapacity = getNextPrime(libraryCapacity * RESIZE);
        Book[] newTable = new Book[newCapacity];
        Arrays.fill(newTable, null);

        for (Book entry : libraryEntries) {
            if (entry != null) {
                int key = entry.getKey();
                String value = entry.getValue();
                int newIndex = primaryHash(key);
                int sizer = 0;

                while (newTable[newIndex] != null) {
                    newIndex = doubleHashing(key, sizer);
                    sizer++;
                }
                newTable[newIndex] = new Entry(key, value);
            }
        }
        libraryEntries = newTable;
        libraryCapacity = newCapacity;
    }
}

public class GroupProject {
    public static void main(String[] args) {
        System.out.println("Hello");

        Book b0, b1, b2, b3, b4, b5, b6, b7, b8, b9;

        LibrarySystem library = new LibrarySystem();

        b0 = new Book("book0", "author0", "publication0", "synopsis0", "isbn0", 0);
        b1 = new Book("book1", "author1", "publication1", "synopsis1", "isbn1", 0);
        b2 = new Book("book2", "author2", "publication2", "synopsis2", "isbn2", 0);
        b3 = new Book("book3", "author3", "publication3", "synopsis3", "isbn3", 0);
        b4 = new Book("book4", "author4", "publication4", "synopsis4", "isbn4", 0);
        b5 = new Book("book5", "author5", "publication5", "synopsis5", "isbn5", 0);
        b6 = new Book("book6", "author6", "publication6", "synopsis6", "isbn6", 0);
        b7 = new Book("book7", "author7", "publication7", "synopsis7", "isbn7", 0);
        b8 = new Book("book8", "author8", "publication8", "synopsis8", "isbn8", 0);
        b9 = new Book("book9", "author9", "publication9", "synopsis9", "isbn9", 0);

        library.addBookData(b0);
        library.addBookData(b1);
        library.addBookData(b2);
        library.addBookData(b3);
        library.addBookData(b4);
        library.addBookData(b5);
        library.addBookData(b6);
        library.addBookData(b7);
        library.addBookData(b8);
        library.addBookData(b9);

        System.out.println(library.booksInTheLibrary());

        library.deleteBookData(b3);
        library.deleteBookData(b5);
        library.deleteBookData(b7);
        library.deleteBookData(b9);

        System.out.println(library.booksInTheLibrary());

        System.out.println(library.searchBook(b0));
    }
}
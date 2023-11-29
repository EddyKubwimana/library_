/**
 * Welcome to the Library Management System Project in Java!
 *
 * This project aims to develop a straightforward yet robust in-memory system for managing a library's catalogue. The system
 * facilitates various operations such as adding books, checking them in and out, searching for books, and efficiently
 * managing book availability. Leveraging key data structures, including hash tables, queues, and binary trees, we aim to
 * deliver an organized and user-friendly environment for both library staff and patrons.
 *
 * **Project Collaborators:**
 *
 * - **Eddy Kubwimana
 * - ** Clovis Mushagalusa Cirubakadhera
 * - ** Cajetan Songwae
 * - ** Muhammed Habib Soumahoro
 * - *Email:* eddy.kubwimana@ashesi.edu.gh
 * - *Email :
 * - *Email :
 * - *Email :
 * 

 *   - *GitHub:* https://github.com/EddyKubwimana/Book-catalogue-management
 *
 * - **UI/UX Designer:** Jane Smith
 *   - *Email:* eddy.kubwimana@ashesi.edu.gh
 *
 * **Core Features and Design Choices:**
 *
 * 1. **Catalogue Management:**
 *    - Utilizes a hash table for efficient management of the book catalogue, ensuring quick retrieval of book information.
 *
 * 2. **Checkout System:**
 *    - Implements a queue system to handle book checkouts and returns in a first-come, first-served manner.
 *
 * 3. **Search Functionality:**
 *    - Empowers users to search for books by title, author, or ISBN using efficient search algorithms for a seamless experience.
 *
 * 4. **Book Availability:**
 *    - Utilizes a binary tree to keep track of available and checked-out books, ensuring efficient updates on book status.
 *
 * 5. **User Interface:**
 *    - Provides a simple text-based Command Line Interface (CLI) for users to interact with the library management functionalities.
 *
 * **Expected Outcomes:**
 *
 * - Demonstrates the appropriate use of data structures for efficient book management and user interactions.
 * - Handles book checkouts and availability updates in real-time, providing users with up-to-date information.
 * - Provides comprehensive documentation explaining design choices and complexity analysis for the implemented features.
 *
 * We look forward to the successful development of this Library Management System, contributing to an organized and
 * user-friendly environment for library patrons. For updates and contributions, please refer to the GitHub repository:
 * https://github.com/EddyKubwimana/Book-catalogue-management
 *
 * Thank you for your collaboration and dedication to this project!
 * 
 */





import java.util.*;

public class Book {

    // Fields
    private String title;        // Book title
    private String author;       // Book author
    private String publication;  // Company of publication
    private String synopsis;     // Book synopsis
    private String version;      // Edition information (e.g., 1st Edition)
    private String isbn;         // International Standard Book Number
    private int edition;         // Edition number

    /**
     * Constructs a new `Book` object with the specified attributes.
     * 
     * @param title       The title of the book.
     * @param author      The author of the book.
     * @param publication The company or entity responsible for publication.
     * @param synopsis    A brief summary or description of the book.
     * @param isbn        The International Standard Book Number of the book.
     */
    public Book(String title, String author, String publication, String synopsis, String isbn) {
        this.title = title;
        this.author = author;
        this.publication = publication;
        this.synopsis = synopsis;
        this.isbn = isbn;
    }

    /**
     * Retrieves the title of the book.
     * 
     * @return The title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves the author of the book.
     * 
     * @return The author of the book.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Retrieves the company of publication for the book.
     * 
     * @return The company of publication.
     */
    public String getPublication() {
        return publication;
    }

    /**
     * Retrieves a brief summary or description of the book.
     * 
     * @return The synopsis of the book.
     */
    public String getSynopsis() {
        return synopsis;
    }

    /**
     * Retrieves the version (edition) information of the book.
     * 
     * @return The version (edition) of the book.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Retrieves the International Standard Book Number (ISBN) of the book.
     * 
     * @return The ISBN of the book.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Retrieves the edition number of the book.
     * 
     * @return The edition number of the book.
     */
    public int getEdition() {
        return edition;
    }

    /**
     * Returns a string representation of the book.
     * 
     * @return A string containing the book title and author.
     */
    @Override
    public String toString() {
        return "Book Title: " + this.title + ", Author: " + this.author;
    }
}
class LibrarySystem {
    
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
        this.libraryEntries = new Book[libraryCapacity];
        this.borrowedBooks = new LinkedList<>();
        this.authors = new Book[libraryCapacity];
        this.booksInTheLibrary = 0;

    }

    // Return the capacity of the table.
    public int libraryStorageCapacity() {
        return libraryEntries.length;
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

        int key = book.numericValue();
        int index = primaryHash(key);
        int addition = 0;

        while (libraryEntries[index] != null) {
            index = doubleHashing(key, addition);
            addition++;
        }

        libraryEntries[index] = book;
        authors[index] = book;
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
import java.util.*;

import javax.print.attribute.standard.MediaSize.ISO;

class Book {
    private String title; // book title
    private String author; // book author
    private String isbn;
    private int edition;

    public Book(String title, String author, String isbn, int edition) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.edition = edition;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getEdition() {
        return edition;
    }

    public String toString() {
        return ("The book " + getTitle() + " written by " + getAuthor());
    }
}

class LibrarySystem {
    private Book[] titles;
    private Book[] authors;
    private Book[] isbns;
    private Queue<Book> borrowedBooks;
    private int libraryCapacity;
    private int booksInTheLibrary;
    private int booksBorrowed;
    private int bookCopies;
    private static final int DEFAULT_CAPACITY = 41;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    private static final int RESIZE = 2;

    public LibrarySystem() {
        this(DEFAULT_CAPACITY);
    }

    public LibrarySystem(int libraryCapacity) {

        this.libraryCapacity = libraryCapacity;
        this.titles = new Book[libraryCapacity];
        this.authors = new Book[libraryCapacity];
        this.isbns = new Book[libraryCapacity];
        this.borrowedBooks = new LinkedList<>();
        this.booksInTheLibrary = 0;
        this.booksBorrowed = 0;

    }

    // Return the capacity of the table.
    public int libraryStorageCapacity() {
        return titles.length;
    }

    // Return the number of elements in the table.
    public int booksInTheLibrary() {
        return booksInTheLibrary;
    }

    // Return the number of borrowed books
    public int booksBorrowed() {
        return booksBorrowed;
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
    public boolean addABook(Book book) {

        if (loadFactor() >= LOAD_FACTOR_THRESHOLD) {
            resizeTheLibrary();
        }

        int key0 = Math.abs(book.getTitle().hashCode());
        int key1 = Math.abs(book.getAuthor().hashCode());
        int key2 = Math.abs(book.getIsbn().hashCode());

        int index0 = primaryHash(key0);
        int index1 = primaryHash(key1);
        int index2 = primaryHash(key2);
        int addition0 = 0;
        int addition1 = 0;
        int addition2 = 0;

        while (titles[index0] != null) {
            index0 = doubleHashing(key0, addition0);
            addition0++;
        }

        while (authors[index1] != null) {
            index1 = doubleHashing(key1, addition1);
            addition1++;
        }

        while (isbns[index2] != null) {
            index2 = doubleHashing(key2, addition2);
            addition2++;
        }

        titles[index0] = book;
        authors[index1] = book;
        isbns[index2] = book;
        booksInTheLibrary++;

        return true;
    }

    // Delete book from the hash table (library).
    public boolean BorrowABook(Book book) {
        int key = Math.abs(book.getTitle().hashCode());
        int index = primaryHash(key);
        int deletion0 = 0;
        int deletion1 = 0;

        while (titles[index] != null) {
            if (titles[index].equals(book)) {
                enqueue(book);
                titles[index] = null;
                booksInTheLibrary--;
                return true;
            }
            index = doubleHashing(key, deletion0);
            deletion0++;
        }

        while (authors[index] != null) {
            if (authors[index].equals(book)) {
                enqueue(book);
                authors[index] = null;
                booksInTheLibrary--;
                return true;
            }
            index = doubleHashing(key, deletion1);
            deletion1++;
        }
        return false;
    }

    // Search the book using its title.
    public Book searchBookByTitle(String title) {
        int key = Math.abs(title.hashCode());
        int primaryIndex = primaryHash(key);
        int secondaryIndex = secondaryHash(key);
        int retrieve = 0;

        while (retrieve < libraryCapacity) {
            int index = (primaryIndex + retrieve * secondaryIndex) % libraryCapacity;

            if (titles[index] == null) {
                System.out.println("Key not found!!");
                return null;
            }

            else if (titles[index].getTitle().equals(title)) {
                return titles[index];
            }
            retrieve++;
        }
        System.out.println("Key not found!!");
        return null;
    }

    // Search the book using its authors.
    public Book searchBookByAuthor(String author) {
        int key = Math.abs(author.hashCode());
        int primaryIndex = primaryHash(key);
        int secondaryIndex = secondaryHash(key);
        int retrieve = 0;

        while (retrieve < libraryCapacity) {
            int index = (primaryIndex + retrieve * secondaryIndex) % libraryCapacity;

            if (authors[index] == null) {
                System.out.println("Key not found!!");
                return null;
            }

            else if (authors[index].getTitle().equals(author)) {
                return authors[index];
            }
            retrieve++;
        }
        System.out.println("Key not found!!");
        return null;
    }

    // Add a book to the end of the queue.
    private void enqueue(Book book) {
        borrowedBooks.add(book);
        booksBorrowed++;
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
    private void resizeTheLibrary() {
        int newCapacity = getNextPrime(libraryCapacity * RESIZE);
        Book[] newTitles = new Book[newCapacity];
        Bokk[] newAuthors = new Book[newCapacity];
        Book[] newIsbns = new Book[newCapacity];
        Arrays.fill(newTitles, null);
        Arrays.fill(newAuthors, null);
        Arrays.fill(newIsbns, null);

        for (Book book : titles) {
            if (book != null) {
                int key = Math.abs(book.getTitle().hashCode());
                int newIndex = primaryHash(key);
                int sizer = 0;

                while (newTitles[newIndex] != null) {
                    newIndex = doubleHashing(key, sizer);
                    sizer++;
                }
                newTitles[newIndex] = book;
            }
        }

        for (Book book : authors) {
            if (book != null) {
                int key = Math.abs(book.getAuthor().hashCode());
                int newIndex = primaryHash(key);
                int sizer = 0;

                while (newAuthors[newIndex] != null) {
                    newIndex = doubleHashing(key, sizer);
                    sizer++;
                }
                newAuthors[newIndex] = book;
            }
        }

        for (Book book : isbns) {
            if (book != null) {
                int key = Math.abs(book.getIsbn().hashCode());
                int newIndex = primaryHash(key);
                int sizer = 0;

                while (newIsbns[newIndex] != null) {
                    newIndex = doubleHashing(key, sizer);
                    sizer++;
                }
                newIsbns[newIndex] = book;
            }
        }
        titles = newTitles;
        authors = newAuthors;
        isbns = newIsbns;
        libraryCapacity = newCapacity;
    }

    public Book[] getBooksInLibrary() {
        Book[] available = new Book[libraryCapacity];
        for (Book book : titles) {
            for (int b = 0; b < libraryCapacity; b++) {
                available[b] = book;
                available[b].toString();
            }
            return available;
        }
        return null;
    }

    public Book[] getBorrowedBooks() {
        Book[] borrowed = new Book[booksBorrowed];

        for (Book book : borrowedBooks) {
            for (int b = 0; b < booksBorrowed; b++) {
                borrowed[b] = book;
                borrowed[b].toString();
            }
            return borrowed;
        }
        return null;
    }
}

public class GroupProject {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello");

        Book b0, b1, b2, b3, b4, b5, b6, b7, b8, b9;

        LibrarySystem library = new LibrarySystem();

        b0 = new Book("book0", "author0", "isbn0", 0);
        b1 = new Book("book1", "author1", "isbn1", 0);
        b2 = new Book("book2", "author2", "isbn2", 0);
        b3 = new Book("book3", "author3", "isbn3", 0);
        b4 = new Book("book4", "author4", "isbn4", 0);
        b5 = new Book("book5", "author5", "isbn5", 0);
        b6 = new Book("book6", "author6", "isbn6", 0);
        b7 = new Book("book7", "author7", "isbn7", 0);
        b8 = new Book("book8", "author8", "isbn8", 0);
        b9 = new Book("book9", "author9", "isbn9", 0);

        library.addABook(b0);
        library.addABook(b1);
        library.addABook(b2);
        library.addABook(b3);
        library.addABook(b4);
        library.addABook(b5);
        library.addABook(b6);
        library.addABook(b7);
        library.addABook(b8);
        library.addABook(b9);

        // Menu-driven interface for the task management system
        while (true) {
            System.out.println();
            System.out.println("Library Management System Menu:");
            System.out.println();
            System.out.println("1. Add a book in the library.");
            System.out.println("2. Borrow a book.");
            System.out.println("3. Search a book.");
            System.out.println("4. View all the books in the library.");
            System.out.println("5. View all borrowed books.");
            System.out.println("6. Find the number of books in the library.");
            System.out.println("7. Find the number of books borrowed.");
            System.out.println("8. Quit!");
            System.out.println();
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.println();

            // Switch case to handle user input and perform corresponding actions
            switch (choice) {
                case 1:
                    while (true) {
                        System.out.println("Are you going to use an 'object' or 'attributes'?");
                        System.out.println("1. I am using object.");
                        System.out.println("2. I am using attributes.");
                        System.out.println();
                        System.out.print("Type your choice here: ");

                        int userFeedback = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println();

                        switch (userFeedback) {
                            case 1:
                                System.out.println("Type the book object.");
                                System.out.print("Type it here: ");
                                String user = scanner.nextLine();
                                System.out.println("We will fix it" + user);
                                // Book aBook = scanner.next();
                                // library.addABook(aBook);
                                break;
                            case 2:
                                System.out.print("Type the book's title: ");
                                String addTitle = scanner.nextLine();

                                System.out.print("Type the author's name: ");
                                String addAuthor = scanner.nextLine();

                                System.out.print("Type the isbn: ");
                                String addIsbn = scanner.nextLine();

                                System.out.print("Type the book's edition: ");
                                int addEdition = scanner.nextInt();

                                scanner.nextLine(); // Consume newline
                                Book addBook = new Book(addTitle, addAuthor, addIsbn, addEdition);
                                library.addABook(addBook);
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    }

                case 2:
                    while (true) {
                        System.out.println("Are you going to use an 'object' or 'attributes'?");
                        System.out.println("1. I am using object.");
                        System.out.println("2. I am using attributes.");
                        System.out.println();
                        System.out.print("Type your choice here: ");

                        int userFeedback = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println();

                        switch (userFeedback) {
                            case 1:
                                System.out.println("Type the book object.");
                                System.out.print("Type it here: ");
                                String user = scanner.nextLine();
                                System.out.println("We will fix it" + user);
                                // Book bBook = scanner.next();
                                // library.BorrowABook(bBook);
                                break;
                            case 2:
                                System.out.println("Enter the details of the book.");
                                System.out.println();
                                System.out.print("Type the book's title: ");
                                String borrowTitle = scanner.nextLine();

                                System.out.print("Type the author's name: ");
                                String borrowAuthor = scanner.nextLine();

                                System.out.print("Type the isbn: ");
                                String borrowIsbn = scanner.nextLine();

                                System.out.print("Type the book's edition: ");
                                int borrowEdition = scanner.nextInt();

                                scanner.nextLine(); // Consume newline
                                Book borrowBook = new Book(borrowTitle, borrowAuthor, borrowIsbn, borrowEdition);
                                library.BorrowABook(borrowBook);
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    }

                case 3:
                    while (true) {
                        System.out.println("What do you want to you for search the book?");
                        System.out.println("1. Book title.");
                        System.out.println("2. Book author.");
                        System.out.println("3. Book isbn.");
                        System.out.println();

                        System.out.print("Type your choice here: ");

                        int userFeedback = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println();

                        switch (userFeedback) {
                            case 1:
                                System.out.println("Enter the book title here: ");
                                String btitle = scanner.nextLine();
                                library.searchBookByTitle(btitle);
                                break;
                            case 2:
                                System.out.println("Enter the book author here: ");
                                String bauthor = scanner.nextLine();
                                library.searchBookByTitle(bauthor);
                                break;
                            case 3:
                                System.out.println("Enter the book isbn here: ");
                                String bisbn = scanner.nextLine();
                                library.searchBookByTitle(bisbn);
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    }
                case 4:
                    System.out.println("The books available in the library.");
                    library.getBooksInLibrary();
                    break;
                case 5:
                    System.out.println("The books borrowed.");
                    library.getBorrowedBooks();
                    break;
                case 6:
                    int numberOfBooks = library.booksInTheLibrary();
                    System.out.println("There are " + numberOfBooks + " books in the library.");
                    break;
                case 7:
                    int borrowedbooks = library.booksBorrowed();
                    System.out.println(borrowedbooks + " from our library are borrowed.");
                    break;
                case 8:
                    System.out.println("Exiting Library Management System. Goodbye!");
                    System.exit(0); // Exits the program
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        // System.out.println(library.booksInTheLibrary());

        // library.BorrowABook(b3);
        // library.BorrowABook(b5);
        // library.BorrowABook(b7);
        // library.BorrowABook(b9);

        // System.out.println(library.booksInTheLibrary());

        // System.out.println(library.searchBookByTitle("book0").getAuthor());

        // Book[] books = library.getBorrowedBooks();

        // System.out.println(books.length);

        // for (Book book : books) {
        // System.out.println(book.getAuthor());
        // }
    }
}
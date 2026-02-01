import service.*;
import model.*;
import utils.DatabaseConnection;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        AuthorService authorService = new AuthorService();
        BookService bookService = new BookService();

        System.out.println("===== LIBRARY MANAGEMENT DEMO =====");

        Author author = authorService.getAll().get(0);
        System.out.println("Using author: " + author.getFullName());

        BookBase book = new PrintedBook(
                0,
                "Demo Book",
                5000,
                2024,
                author,
                250,
                "A-10"
        );

        book = bookService.create(book);
        System.out.println("\nCREATED:");
        System.out.println(book.printInfo());

        System.out.println("\nALL BOOKS:");
        List<BookBase> books = bookService.getAll();
        books.forEach(b -> System.out.println(b.printInfo()));

        book.setPrice(9999);
        bookService.update(book.getId(), book);

        System.out.println("\nUPDATED:");
        System.out.println(bookService.getById(book.getId()).printInfo());

        bookService.delete(book.getId());
        System.out.println("\nDELETED book id = " + book.getId());

        try {
            bookService.create(new EBook(0, "", 0, 2024, author, 1.2, "link"));
        } catch (Exception e) {
            System.out.println("\nVALIDATION ERROR → " + e.getClass().getSimpleName());
        }

        try {
            bookService.getById(999999);
        } catch (Exception e) {
            System.out.println("NOT FOUND ERROR → " + e.getClass().getSimpleName());
        }

        System.out.println("\n===== DEMO FINISHED SUCCESSFULLY =====");
    }
}

package service;

import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.BookBase;
import repository.BookRepository;

import java.util.List;

public class BookService {

    private final BookRepository repository = new BookRepository();

    public BookBase create(BookBase book) {
        if (book == null) {
            throw new InvalidInputException("Book cannot be null");
        }

        // validate() пока может кидать IllegalArgumentException внутри модели — это нормально,
        // но если хочешь, позже тоже заменим на InvalidInputException в model.
        book.validate();

        if (book.getAuthor() == null || book.getAuthor().getId() <= 0) {
            throw new InvalidInputException("Author must exist (valid author_id required)");
        }

        return repository.create(book);
    }

    public List<BookBase> getAll() {
        return repository.getAll();
    }

    public BookBase getById(int id) {
        BookBase b = repository.getById(id);
        if (b == null) {
            throw new ResourceNotFoundException("Book not found");
        }
        return b;
    }

    public boolean update(int id, BookBase book) {
        if (book == null) {
            throw new InvalidInputException("Book cannot be null");
        }

        book.validate();

        boolean updated = repository.update(id, book);
        if (!updated) {
            throw new ResourceNotFoundException("Book not found for update");
        }
        return true;
    }

    public boolean delete(int id) {
        boolean deleted = repository.delete(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Book not found for delete");
        }
        return true;
    }
}
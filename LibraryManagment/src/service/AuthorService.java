package service;

import model.Author;
import repository.AuthorRepository;

import java.util.List;

import exception.InvalidInputException;
import exception.ResourceNotFoundException;

public class AuthorService {

    private final AuthorRepository repository = new AuthorRepository();

    public Author create(Author author) {
        if (author.getFullName() == null || author.getFullName().isBlank()) {
            throw new InvalidInputException("Author name cannot be empty");
        }
        return repository.create(author);
    }

    public List<Author> getAll() {
        return repository.getAll();
    }

    public Author getById(int id) {
        Author a = repository.getById(id);
        if (a == null) {
            throw new ResourceNotFoundException("Author not found");
        }
        return a;
    }

    public boolean delete(int id) {
        return repository.delete(id);
    }
}
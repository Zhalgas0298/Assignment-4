package repository;

import exception.DatabaseOperationException;
import exception.DuplicateResourceException;

import model.Author;
import model.BookBase;
import model.EBook;
import model.PrintedBook;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    // =====================
    // CREATE
    // =====================
    public BookBase create(BookBase book) {
        String sql = """
            INSERT INTO books
              (title, publish_year, price, book_type, author_id,
               file_size_mb, download_url, pages, shelf_code)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING book_id
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            fillInsertOrUpdateStatement(ps, book, book.getAuthor().getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int newId = rs.getInt("book_id");
                    return copyWithId(book, newId);
                }
            }
            throw new RuntimeException("Failed to insert book (no id returned).");

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new DuplicateResourceException("Duplicate book (same title for the same author).");
            }
            throw new DatabaseOperationException("DB error in BookRepository: " + e.getMessage(), e);
        }

    }

    // =====================
    // GET ALL
    // =====================
    public List<BookBase> getAll() {
        String sql = """
            SELECT
              b.book_id, b.title, b.publish_year, b.price, b.book_type,
              b.file_size_mb, b.download_url, b.pages, b.shelf_code,
              a.author_id, a.full_name, a.country
            FROM books b
            JOIN authors a ON a.author_id = b.author_id
            ORDER BY b.book_id
            """;

        List<BookBase> books = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
            return books;

        } catch (SQLException e) {
            // 23505 = unique_violation (например UNIQUE(title, author_id))
            if ("23505".equals(e.getSQLState())) {
                throw new DuplicateResourceException("Duplicate book (same title for the same author).");
            }
            throw new DatabaseOperationException("DB error in BookRepository: " + e.getMessage(), e);
        }

    }

    // =====================
    // GET BY ID
    // =====================
    public BookBase getById(int id) {
        String sql = """
            SELECT
              b.book_id, b.title, b.publish_year, b.price, b.book_type,
              b.file_size_mb, b.download_url, b.pages, b.shelf_code,
              a.author_id, a.full_name, a.country
            FROM books b
            JOIN authors a ON a.author_id = b.author_id
            WHERE b.book_id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToBook(rs);
                }
            }
            return null; // позже заменим на ResourceNotFoundException

        } catch (SQLException e) {
            // 23505 = unique_violation (например UNIQUE(title, author_id))
            if ("23505".equals(e.getSQLState())) {
                throw new DuplicateResourceException("Duplicate book (same title for the same author).");
            }
            throw new DatabaseOperationException("DB error in BookRepository: " + e.getMessage(), e);
        }

    }

    // =====================
    // UPDATE
    // =====================
    public boolean update(int id, BookBase book) {
        String sql = """
            UPDATE books
            SET title = ?, publish_year = ?, price = ?, book_type = ?, author_id = ?,
                file_size_mb = ?, download_url = ?, pages = ?, shelf_code = ?
            WHERE book_id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // первые 9 параметров — как в INSERT
            fillInsertOrUpdateStatement(ps, book, book.getAuthor().getId());
            // 10-й параметр — id книги для WHERE
            ps.setInt(10, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new DuplicateResourceException("Duplicate book (same title for the same author).");
            }
            throw new DatabaseOperationException("DB error in BookRepository: " + e.getMessage(), e);
        }

    }

    // =====================
    // DELETE
    // =====================
    public boolean delete(int id) {
        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("DB error while deleting book: " + e.getMessage(), e);
        }
    }

    // =====================
    // Helpers
    // =====================

    // Заполняет параметры 1..9 для INSERT и UPDATE (в одном месте, чтобы не повторяться)
    private void fillInsertOrUpdateStatement(PreparedStatement ps, BookBase book, int authorId) throws SQLException {
        ps.setString(1, book.getTitle());
        ps.setInt(2, book.getPublishYear());
        ps.setDouble(3, book.getPrice());
        ps.setString(4, book.getFormat()); // "EBOOK" или "PRINTED"
        ps.setInt(5, authorId);

        if (book instanceof EBook eb) {
            ps.setDouble(6, eb.getFileSizeMb());
            ps.setString(7, eb.getDownloadUrl());
            ps.setObject(8, null); // pages
            ps.setObject(9, null); // shelf_code
        } else if (book instanceof PrintedBook pb) {
            ps.setObject(6, null); // file_size_mb
            ps.setObject(7, null); // download_url
            ps.setInt(8, pb.getPages());
            ps.setString(9, pb.getShelfCode());
        } else {
            ps.setObject(6, null);
            ps.setObject(7, null);
            ps.setObject(8, null);
            ps.setObject(9, null);
        }
    }

    private BookBase mapRowToBook(ResultSet rs) throws SQLException {
        Author author = new Author(
                rs.getInt("author_id"),
                rs.getString("full_name"),
                rs.getString("country")
        );

        int id = rs.getInt("book_id");
        String title = rs.getString("title");
        int year = rs.getInt("publish_year");
        double price = rs.getDouble("price");
        String type = rs.getString("book_type");

        if ("EBOOK".equals(type)) {
            double fileSize = rs.getDouble("file_size_mb");
            String url = rs.getString("download_url");
            return new EBook(id, title, price, year, author, fileSize, url);
        } else { // PRINTED
            int pages = rs.getInt("pages");
            String shelf = rs.getString("shelf_code");
            return new PrintedBook(id, title, price, year, author, pages, shelf);
        }
    }

    private BookBase copyWithId(BookBase book, int newId) {
        Author a = book.getAuthor();

        if (book instanceof EBook eb) {
            return new EBook(newId, eb.getTitle(), eb.getPrice(), eb.getPublishYear(), a,
                    eb.getFileSizeMb(), eb.getDownloadUrl());
        }
        if (book instanceof PrintedBook pb) {
            return new PrintedBook(newId, pb.getTitle(), pb.getPrice(), pb.getPublishYear(), a,
                    pb.getPages(), pb.getShelfCode());
        }
        return book;
    }
}
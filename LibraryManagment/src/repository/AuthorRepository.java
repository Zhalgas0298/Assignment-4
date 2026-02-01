package repository;

import exception.DatabaseOperationException;
import exception.DuplicateResourceException;

import model.Author;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepository {

    // CREATE
    public Author create(Author author) {
        String sql = "INSERT INTO authors(full_name, country) VALUES (?, ?) RETURNING author_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, author.getFullName());
            ps.setString(2, author.getCountry());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int newId = rs.getInt("author_id");
                    return new Author(newId, author.getFullName(), author.getCountry());
                }
            }

            throw new RuntimeException("Failed to insert author (no id returned).");

        } catch (SQLException e) {
            throw new RuntimeException("DB error while creating author: " + e.getMessage(), e);
        }
    }

    // GET ALL
    public List<Author> getAll() {
        String sql = "SELECT author_id, full_name, country FROM authors ORDER BY author_id";
        List<Author> authors = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                authors.add(new Author(
                        rs.getInt("author_id"),
                        rs.getString("full_name"),
                        rs.getString("country")
                ));
            }

            return authors;

        } catch (SQLException e) {
            throw new RuntimeException("DB error while reading authors: " + e.getMessage(), e);
        }
    }

    // GET BY ID
    public Author getById(int id) {
        String sql = "SELECT author_id, full_name, country FROM authors WHERE author_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Author(
                            rs.getInt("author_id"),
                            rs.getString("full_name"),
                            rs.getString("country")
                    );
                }
            }

            return null; // позже заменим на ResourceNotFoundException

        } catch (SQLException e) {
            throw new RuntimeException("DB error while reading author: " + e.getMessage(), e);
        }
    }

    // UPDATE
    public boolean update(int id, Author author) {
        String sql = "UPDATE authors SET full_name = ?, country = ? WHERE author_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, author.getFullName());
            ps.setString(2, author.getCountry());
            ps.setInt(3, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("DB error while updating author: " + e.getMessage(), e);
        }
    }

    // DELETE
    public boolean delete(int id) {
        String sql = "DELETE FROM authors WHERE author_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // 23505 = unique_violation в PostgreSQL (дубликат)
            if ("23505".equals(e.getSQLState())) {
                throw new DuplicateResourceException("Duplicate author (full_name must be unique).");
            }
            throw new DatabaseOperationException("DB error in AuthorRepository: " + e.getMessage(), e);
        }

    }
}
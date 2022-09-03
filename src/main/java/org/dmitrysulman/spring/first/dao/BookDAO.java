package org.dmitrysulman.spring.first.dao;

import org.dmitrysulman.spring.first.models.Book;
import org.dmitrysulman.spring.first.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM book ORDER BY name", new BeanPropertyRowMapper<>(Book.class));
    }

    public Optional<Book> show(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id=?", new BeanPropertyRowMapper<>(Book.class), id)
                .stream()
                .findAny();
    }

    public int create(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO book (name, author, year) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getYear());
            return preparedStatement;
        }, keyHolder);
        return (int) Objects.requireNonNull(keyHolder.getKeys()).get("id");
    }

    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE book SET name=?, author=?, year=? WHERE id=?",
                book.getName(), book.getAuthor(), book.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }

    public void assign(int bookId, int personId) {
        jdbcTemplate.update("UPDATE book SET person_id=? WHERE id=?",
                personId, bookId);
    }

    public void release(int id) {
        jdbcTemplate.update("UPDATE book SET person_id=NULL WHERE id=?", id);
    }
}

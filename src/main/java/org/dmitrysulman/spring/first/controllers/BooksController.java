package org.dmitrysulman.spring.first.controllers;

import org.dmitrysulman.spring.first.dao.BookDAO;
import org.dmitrysulman.spring.first.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookDAO bookDAO;

    @Autowired
    public BooksController(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id) {
        Optional<Book> book = bookDAO.show(id);
        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        } else {
            model.addAttribute("book", book.get());
        }
        return "books/show";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("book") Book book) {
        return "books/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/add";
        }

        int id = bookDAO.create(book);
        return "redirect:/books/" + id;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        Optional<Book> book = bookDAO.show(id);
        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        } else {
            model.addAttribute("book", book.get());
        }

        return "books/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@ModelAttribute @Valid Book book, @PathVariable("id") int id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        bookDAO.update(id, book);
        return "redirect:/books/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        Optional<Book> book = bookDAO.show(id);
        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        } else {
            bookDAO.delete(id);
        }
        return "redirect:/books/";
    }
}

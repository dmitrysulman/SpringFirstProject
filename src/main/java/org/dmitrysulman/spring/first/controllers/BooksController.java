package org.dmitrysulman.spring.first.controllers;

import org.dmitrysulman.spring.first.dao.BookDAO;
import org.dmitrysulman.spring.first.dao.PersonDAO;
import org.dmitrysulman.spring.first.models.Book;
import org.dmitrysulman.spring.first.models.Person;
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
    private final PersonDAO personDAO;

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookDAO.index());

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id, @ModelAttribute Person person) {
        Optional<Book> book = bookDAO.show(id);
        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        } else {
            Optional<Person> bookOwner = bookDAO.getBookOwner(id);
            if (bookOwner.isPresent()) {
                model.addAttribute("owner", bookOwner.get());
            } else {
                model.addAttribute("people", personDAO.index());
            }
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

    @PostMapping("/{id}/assign")
    public String assign(@ModelAttribute Person person, @PathVariable("id") int id) {
        bookDAO.assign(id, person);

        return "redirect:/books/" + id;
    }

    @PostMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        Optional<Book> book = bookDAO.show(id);
        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        } else {
            bookDAO.release(id);
        }

        return "redirect:/books/" + id;
    }
}

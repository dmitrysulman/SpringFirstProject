package org.dmitrysulman.spring.first.controllers;

import org.dmitrysulman.spring.first.dao.PersonDAO;
import org.dmitrysulman.spring.first.models.Person;
import org.dmitrysulman.spring.first.util.PersonValidator;
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
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonDAO personDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id) {
        Optional<Person> person = personDAO.show(id);
        if (person.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Человек не найден");
        } else {
            model.addAttribute("person", person.get());
        }
        return "people/show";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("person") Person person) {
        return "people/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "people/add";
        }

        int id = personDAO.add(person);
        return "redirect:/people/" + id;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        Optional<Person> person = personDAO.show(id);
        if (person.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Человек не найден");
        } else {
            model.addAttribute("person", person.get());
        }

        return "people/edit";
    }
}

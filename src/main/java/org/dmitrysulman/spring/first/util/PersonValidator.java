package org.dmitrysulman.spring.first.util;

import org.dmitrysulman.spring.first.dao.PersonDAO;
import org.dmitrysulman.spring.first.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> personWithSameName = personDAO.show(person.getFullName());
        if (personWithSameName.isPresent() && (person.getId() == 0 || person.getId() != personWithSameName.get().getId())) { //!personDAO.show(person.getId()).get().getFullName().equals(person.getFullName()))) {
            errors.rejectValue("fullName", "", "This name already exist");
        }
    }
}

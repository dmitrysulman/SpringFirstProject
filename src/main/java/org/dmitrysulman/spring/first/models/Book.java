package org.dmitrysulman.spring.first.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Book {
    private int id;

    private int person_id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 3, max = 200, message = "Name should be between 3 and 200")
    private String name;

    @NotEmpty(message = "Author should not be empty")
    @Size(min = 3, max = 400, message = "Author should be between 3 and 400")
    private String author;

    @Min(value = 1900, message = "Year of birth should be greater than 1899")
    private int year;

    public Book(int id, int person_id, String name, String author, int year) {
        this.id = id;
        this.person_id = person_id;
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

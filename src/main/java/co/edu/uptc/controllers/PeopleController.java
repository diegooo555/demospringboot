package co.edu.uptc.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uptc.dtos.PersonDto;
import co.edu.uptc.models.PersonModel;
import co.edu.uptc.services.PeopleManagerService;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/prog2/202322204/people")
public class PeopleController {

    @Autowired
    PeopleManagerService peopleManagerService;

    @GetMapping("/all")
    public ArrayList<PersonModel> getAll() {
        ArrayList<PersonModel> persons = new ArrayList<>();
        try {
            persons = peopleManagerService.getPeople();
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return persons;
    }


    @GetMapping("/id/{id}")
    public PersonDto getById(@PathVariable Long id) {
        PersonDto personFind = new PersonDto();
        try {
            personFind = PersonDto.toPersonDto(peopleManagerService.getPerson(id));
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return personFind;
    }

    @PostMapping("/add")
    public PersonModel addPerson(@RequestBody PersonModel person) {
        PersonModel newPerson = new PersonModel();
        try {
            newPerson = peopleManagerService.addPerson(person);
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return newPerson;
    }

    @DeleteMapping("/{id}")
    public PersonModel deletePerson(@PathVariable Long id) {
        PersonModel personModel = null;
        try {
            personModel = peopleManagerService.deletePerson(id);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return personModel;
    }

}

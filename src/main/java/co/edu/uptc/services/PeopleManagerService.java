package co.edu.uptc.services;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import co.edu.uptc.models.PersonModel;
import co.edu.uptc.models.PersonModel.Genders;

@Service
public class PeopleManagerService {

    private ArrayList<PersonModel> people;
    private Long ids=0L;

    public PeopleManagerService(){
        people = new ArrayList<>();

        // TODO  this is temporary
        fillPeople();
    }
    
    public void addPerson(PersonModel person){
        person.setId(ids);
        ids++;
        people.add(person);
    }


    public ArrayList<PersonModel> getPeople(){
        return people;
    }

   public PersonModel deletePerson(Long id){
    PersonModel auxPersonModel=getPerson(id);    
    if (auxPersonModel!=null) {
        people.remove(auxPersonModel);
    }
    return auxPersonModel;
   }

   public PersonModel getPerson(Long id){ 
    PersonModel auxPersonModel=null;
   for (PersonModel personModel : people) {
       if (personModel.getId() == id) {
           auxPersonModel = personModel;
       }
   }
   return auxPersonModel;
}


  //TODO This method is temporary, only for testing
    public void fillPeople(){
       PersonModel personModel = new PersonModel();
       personModel.setName("Calos");
       personModel.setLastName("Lopez");
       personModel.setGender(Genders.MALE);
       personModel.setBirthday(LocalDate.of(1990, 12, 31));
       addPerson(personModel);

       personModel = new PersonModel();
       personModel.setName("Maria");
       personModel.setLastName("Perez");
       personModel.setGender(Genders.FEMALE);
       personModel.setBirthday(LocalDate.of(2000, 1, 6));
       addPerson(personModel);


       personModel = new PersonModel();
       personModel.setName("Diego");
       personModel.setLastName("Aponte");
       personModel.setGender(Genders.MALE);
       personModel.setBirthday(LocalDate.of(2010, 3, 7));
       addPerson(personModel);


    }

    
}

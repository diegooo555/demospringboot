package co.edu.uptc.services;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import co.edu.uptc.models.PersonModel;
import co.edu.uptc.models.PersonModel.Genders;

@Service
public class PeopleManagerService {

    @Getter
    private final ObjectMapper mapper;
    private Path pathDirectory;

    @Value("${source.file.persons}")
    String pathFilePersons;

    public PeopleManagerService() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        pathDirectory = Paths.get(System.getProperty("user.dir"));
    }

    public PersonModel addPerson(PersonModel person) throws IOException {
        String name = formatText(person.getName(), 10);
        String lastname = formatText(person.getLastName(), 10);
        String birthday = person.getBirthday().toString();
        String gender = formatText(person.getGender().toString(), 6);
        RandomAccessFile raf = new RandomAccessFile(getAbsPathPersons().toString(), "rw");
        int id = calculateLastId(raf);
        String[] data =  {name, lastname, birthday, gender};
        writePerson(raf, id, data);
        raf.close();
        return newPersonModel(id, name, lastname, birthday, person.getGender().toString());
    }

    private void writePerson(RandomAccessFile raf, int id, String[] data) throws IOException {
        raf.seek(raf.length());
        raf.writeInt(id);
        raf.writeUTF(data[0]);
        raf.writeUTF(data[1]);
        raf.writeUTF(data[2]);
        raf.writeUTF(data[3]);
        raf.writeBoolean(false);
    }

    private int calculateLastId(RandomAccessFile raf) throws IOException {
        int lastId = 0;
        if (raf.length() > 0) {
            lastId = Math.toIntExact(raf.length() / 49);
        }
        return lastId;
    }

    private PersonModel newPersonModel(int id, String name, String lastName, String birthday, String gender) {
        PersonModel person = new PersonModel();
        person.setId((long) id);
        person.setName(name);
        person.setLastName(lastName);
        person.setBirthday(LocalDate.parse(birthday));
        person.setGender(Genders.valueOf(gender));
        return person;
    }


    public ArrayList<PersonModel> getPeople() throws IOException {
        ArrayList<PersonModel> persons = new ArrayList<>();
        RandomAccessFile raf = new RandomAccessFile(getAbsPathPersons().toString(), "rw");
        Long lastId = (raf.length() / 49L) - 1L;
        for (Long actualId = 0L; actualId <= lastId; actualId++) {
            PersonModel actualPerson = getPerson(actualId);
            if (actualPerson != null) {
                persons.add(actualPerson);
            }
        }
        raf.close();
        return persons;
    }

    public PersonModel deletePerson(Long id) throws IOException {
        PersonModel auxPersonModel = getPerson(id);
        if (auxPersonModel != null) {
            RandomAccessFile raf = new RandomAccessFile(getAbsPathPersons().toString(), "rw");
            raf.seek(id * 49 + 48);
            raf.writeBoolean(true);
            raf.close();
        }
        return auxPersonModel;
    }

    public PersonModel getPerson(Long id) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(getAbsPathPersons().toString(), "rw");
        raf.seek(id * 49);
        PersonModel auxPerson = readInfoPerson(raf);
        raf.close();
        return auxPerson;
    }

    private PersonModel readInfoPerson(RandomAccessFile raf) throws IOException {
        PersonModel auxPerson = null;
        int idPerson = raf.readInt();
        String name = removesSpaces(raf.readUTF());
        String lastname = removesSpaces(raf.readUTF());
        String birthday = removesSpaces(raf.readUTF());
        String gender = removesSpaces(raf.readUTF());
        if (!raf.readBoolean()) {
            auxPerson = newPersonModel(idPerson, name, lastname, birthday, gender);
        }
        return auxPerson;
    }

    public static String formatText(String text, int size) {
        if (text.length() < size) {
            return String.format("%-" + size + "s", text);
        } else {
            return text.substring(0, size);
        }
    }

    public String removesSpaces(String chain) {
        return chain.replaceAll("\\s+", "");
    }

    @NotNull
    private Path getAbsPathPersons() {
        return Paths.get(pathDirectory.toString(), pathFilePersons);
    }
}

package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Person;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public List<Person> getAllPeople() {
        List<Person> people = personRepository.findAll();
        Collections.sort(people, Comparator.comparing(Person::getName, String.CASE_INSENSITIVE_ORDER));
        return people;
    }

    @Override
    public Person getPersonById(Long id) {
        Optional<Person> result = personRepository.findById(id);
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get();
        }
    }

    @Override
    public Person savePerson(Person person) {
        Person newPerson = personRepository.save(person);
        personRepository.flush();
        return newPerson;
    }

    @Override
    public void updatePersonRelations(Long oldPersonId, Long newPersonId) {
        personRepository.updatePersonUnits(oldPersonId, newPersonId);
        personRepository.updatePersonItems(oldPersonId, newPersonId);
        personRepository.updatePersonConfigurationRelation(oldPersonId, newPersonId);
        personRepository.updatePersonRole(oldPersonId, newPersonId);
        personRepository.updatePersonIdentities(oldPersonId, newPersonId);
        personRepository.updatePersonCompetencies(oldPersonId, newPersonId);
        personRepository.updatePersonGroups(oldPersonId, newPersonId);
    }

    @Override
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }
}
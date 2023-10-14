package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Person;

import java.util.List;

public interface PersonService {

    /**
     * Method obtaining all people sorted by name
     * @return  List of all people from database
     */
    List<Person> getAllPeople();

    /**
     * Method obtaining person based on id
     * @param id    Identifier of person
     * @return      Person for given id or null
     */
    Person getPersonById(Long id);

    /**
     * Method saving person into database
     * @param person    Person for saving
     * @return          Saved person
     */
    Person savePerson(Person person);

    /**
     * Method for updating all relations with person
     * from the old one to new one
     * @param oldPersonId   Identifier of obsolete person
     * @param newPersonId   Identifier of new person
     */
    void updatePersonRelations(Long oldPersonId, Long newPersonId);

    /**
     * Method for deleting person based on id
     * @param id    Identifier of person
     */
    void deletePerson(Long id);
}

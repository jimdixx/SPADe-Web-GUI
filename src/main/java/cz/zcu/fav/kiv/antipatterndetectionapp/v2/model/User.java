package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import javax.persistence.*;

/**
 * Model of database table represents one user
 * @version 1.0
 * @author Vaclav Hrabik, Jiri Trefil
 */
@Entity
@Table(name = "Users")
public class User {

    /**
     * Max length of column in the table
     */
    @Transient
    private static final int MAX_COLUMN_LENGTH = 255;
    /**
     * Unique key of table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the User
     */
    @Column(unique = true,length = MAX_COLUMN_LENGTH)
    private String name;

    /**
     * Email of the User
     */
    @Column(length = MAX_COLUMN_LENGTH)
    private String email;

    /**
     * Password of the User
     */
    @Column(length = MAX_COLUMN_LENGTH)
    private String password;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }


    /**
     * Getter of id
     * @return id of the user
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter of name
     * @return  name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Getter of email
     * @return  email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for email constraint
     * @return  email constraint
     */
    public static int getEmailConstraint(){
        return MAX_COLUMN_LENGTH;
    }

    /**
     * Getter for name constraint
     * @return  name constraint
     */
    public static int getNameConstraint(){
        return MAX_COLUMN_LENGTH;
    }

    public String getPassword() {
        return this.password;
    }
}

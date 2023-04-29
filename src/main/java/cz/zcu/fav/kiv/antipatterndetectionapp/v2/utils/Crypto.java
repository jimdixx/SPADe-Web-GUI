package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto {

    /**
     * Method to hash password
     * @param password  password from client
     * @return          hashed password for database
     */
    public static String hashString(String password) {
        //standard java security encryption module
        MessageDigest digest = null;
        try {
            //try to instance the class - throws an error if algorithm
            digest = MessageDigest.getInstance("SHA3-256");
        }
        catch(NoSuchAlgorithmException exception){
            exception.printStackTrace();
            return null;
        }
        byte [] tmp = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        //convert byte array into string
        return (new HexBinaryAdapter()).marshal(tmp);
    }

    /**
     * Method compares user password with stored hash
     * @param password String user provided password
     * @param hash String hash saved in database
     * @return true if hashes are the same
     */
    public static boolean compareHashes(String password, String hash) {
        final String passwordHash = hashString(password);
        return hash.equals(passwordHash);
    }


}

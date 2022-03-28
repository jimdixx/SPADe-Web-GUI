package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.controller.AppController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Properties;

@Service
public class UserAccountServiceImpl implements UserAccountService{

    private final String accountDetailsFile = "account.properties";

    private final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @Override
    public boolean checkCredentials(String username, String password) {
        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream(accountDetailsFile)) {

            Properties prop = new Properties();

            if (input == null) {
                LOGGER.error("Unable to open file: " + accountDetailsFile);
                return false;
            }

            // load a properties file
            prop.load(input);

            //get the property value and print it out
            String accountUsername = prop.getProperty("account.user.name");
            String accountPassword = prop.getProperty("account.user.password");

            if(username.equals(accountUsername) && password.equals(accountPassword))
                return true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }
}

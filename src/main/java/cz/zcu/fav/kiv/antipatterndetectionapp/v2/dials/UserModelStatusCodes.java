package cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials;

/**
 * Enum for status codes
 */
public enum UserModelStatusCodes {
    INVALID_USER_ARGUMENTS("Invalid user arguments for registration",400),
    USER_EXISTS("User exists",400),
    HASH_FAILED("Error occurred while creating user",500),
    USER_CREATED("User successfully created",201),
    USER_LOGGED_IN("User logged in successfully",200),
    USER_LOGIN_FAILED("User login failed",401),
    USER_CREATION_FAILED("Error occurred while creating user",503),
    USER_LOGGED_OUT("User logged out.",200);

    public final String label;
    public final int statusCode;

    private UserModelStatusCodes(String s, int i) {
        this.label = s;
        this.statusCode = i;
    }
}
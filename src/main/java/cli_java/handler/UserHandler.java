package cli_java.handler;

public final class UserHandler {

    private static UserHandler instance;

    public final static String NOT_LOGGED_IN = "NONE";
    private static String currentUsername = NOT_LOGGED_IN;

    static {
        try {
            instance = new UserHandler();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate Singleton UserHandler instance!");
        }
    }

    private UserHandler() {

    }

    public static synchronized UserHandler getInstance() {
        return instance;
    }

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void resetUsername() {
        currentUsername = NOT_LOGGED_IN;
    }

    public static boolean isLoggedIn() {
        return !currentUsername.equals(NOT_LOGGED_IN);
    }

}
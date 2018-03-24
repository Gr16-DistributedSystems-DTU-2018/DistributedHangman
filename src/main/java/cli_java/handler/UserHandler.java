package cli_java.handler;

public final class UserHandler {

    private static UserHandler instance;

    public final static String NOT_LOGGED_IN = "NONE";
    private static String currentUsername = NOT_LOGGED_IN;
    private static String currentPassword = NOT_LOGGED_IN;

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

    public static void setCurrentPassword(String password) {
        currentPassword = password;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static String getCurrentPassword() {
        return currentPassword;
    }

    public static void resetPassword() {
        currentPassword = NOT_LOGGED_IN;
    }

    public static void resetUsername() {
        currentUsername = NOT_LOGGED_IN;
    }

    public static boolean isLoggedIn() {
        return !currentUsername.equals(NOT_LOGGED_IN);
    }

}
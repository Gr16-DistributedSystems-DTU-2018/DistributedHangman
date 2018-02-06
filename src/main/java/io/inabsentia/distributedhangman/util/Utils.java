package io.inabsentia.distributedhangman.util;

public final class Utils {

    /* Fields */
    public final static String CMD_ARROW = "-->";
    public final static String RMI_STUB_URL_USERS = "rmi://javabog.dk/brugeradmin";
    public final static String RMI_STUB_URL_LOGIC = "rmi://localhost/gamelogicservice";
    public final static String HIGH_SCORE_FIELD_KEY = "s151641_highscore";

    public static final int SINGLE_CHAR_SCORE = 10;

    public final static int MAXIMUM_LIFE = 6;

    /* Static Singleton instance */
    private static Utils instance;

    /*
     * Static initialization block for the Singleton instance.
     */
    static {
        try {
            instance = new Utils();
        } catch (Exception e) {
            throw new RuntimeException("Fatal error creating Singleton Utils instance!");
        }
    }

    /*
     * Private constructor for Singleton.
     */
    private Utils() {

    }

    /*
     * Singleton instance getter.
     */
    public static synchronized Utils getInstance() {
        return instance;
    }

}
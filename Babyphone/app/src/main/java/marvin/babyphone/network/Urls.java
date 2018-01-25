package marvin.babyphone.network;

import java.util.Locale;

public class Urls {

    private static final String DB_READ             = "https://tutoring-team.de/IOT/db.php?user=%1$s&pass=%2$s&amount=%3$d&mode=r2";
    private static final String DB_CHECK_ACCOUNT    = "https://tutoring-team.de/IOT/db.php?user=%1$s&pass=%2$s&amount=0&mode=r1";
    private static final String DB_DELETE_ENTRIES   = "https://tutoring-team.de/IOT/db.php?user=%1$s&pass=%2$s&mode=d0";
    private static final String DB_DELETE_ACCOUNT   = "https://tutoring-team.de/IOT/db.php?user=%1$s&pass=%2$s&mode=u0";

    public static String DB_READ(String username, String password, long timestamp) {
        return String.format(Locale.getDefault(), DB_READ, username, password, timestamp);
    }

    public static String DB_CHECK_ACCOUNT(String username, String password) {
        return String.format(Locale.getDefault(), DB_CHECK_ACCOUNT, username, password);
    }

    public static String DB_DELETE_ENTRIES(String username, String password) {
        return String.format(Locale.getDefault(), DB_DELETE_ENTRIES, username, password);
    }

    public static String DB_DELETE_ACCOUNT(String username, String password) {
        return String.format(Locale.getDefault(), DB_DELETE_ACCOUNT, username, password);
    }

}

package smartdoor.config;

import java.util.HashMap;

public class DatabaseConfig extends Config {
    /**
     * Map the DB configuration variables
     *
     * @var HashMap
     */
    private static final HashMap<String, String>  databaseConfig = new HashMap<>();

    static {
        databaseConfig.put("host", Config.dotenv.get("DB_HOST", "localhost"));
        databaseConfig.put("user", Config.dotenv.get("DB_USER", "root"));
        databaseConfig.put("password", Config.dotenv.get("DB_PASS", ""));
        databaseConfig.put("dbname", Config.dotenv.get("DB_NAME", "smartdoor"));
        databaseConfig.put("port", Config.dotenv.get("DB_PORT", "3306"));
    }

    /**
     * Get the configuration variable passed in argument
     *
     * @param key
     * @return String
     */
    public static String get(String key) {
        return databaseConfig.getOrDefault(key, null);
    }
}

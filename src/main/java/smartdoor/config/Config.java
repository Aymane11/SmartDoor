package smartdoor.config;

import io.github.cdimascio.dotenv.Dotenv;

import smartdoor.utils.FileSystem;

abstract class Config {
    /**
     * Loads the env variables from the .env file
     *
     * @var Dotenv
     */
    final protected static Dotenv dotenv = Dotenv.configure()
            .directory(FileSystem.getResourcesPath())
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

    public static String get(String key) {
        return null;
    }
}

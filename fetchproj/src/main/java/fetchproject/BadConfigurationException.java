package fetchproject;

/*
 * Exception class for bad cofiguration in the YAML configuration file
 */
public class BadConfigurationException extends Exception{
    public BadConfigurationException(String errorMessage) {
        super(errorMessage);
    }
}

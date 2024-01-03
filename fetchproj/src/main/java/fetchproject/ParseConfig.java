package fetchproject;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.yaml.snakeyaml.Yaml;

/*
 * class for parsing out endpoint objects and maintaining a list of them
 * Uses SnakeYaml open source library to parse YAML to java objects which
 * are then converted to Endpoint objects
 */
public class ParseConfig {

    //list of all endpoints that are parsed out
    public static ArrayList<Endpoint> endpoints = new ArrayList<Endpoint>();

    /*
    *Function to Parse the YAML configuration to Endpoint objects
    *SnakeYaml creates a list of linkedhashmap objects for the schema set in the config file
    *each linkedhashmap represents a block (endpoint) in the configuration. We will cast this
    *to a ArrayList of HashMaps. Keys will be strings, values will be objects, since the
    *header entry is a dictionary
    */
    public static void ParseYaml(String input) throws BadConfigurationException{
        //YAML data type is part of the SnakeYaml library
        Yaml endpointYaml = new Yaml();
        Path path = Paths.get(input);
        try {
        String fileString = Files.readString(path); //read the config file to a string
        //invoke the conversion of the YAML string to Java objects
        ArrayList<HashMap<String, Object>> obj = endpointYaml.load(fileString);
        //create an endpoint objects for each entry and add it to the Endpoint arraylist
        for (HashMap<String, Object> object : obj) {
            Endpoint newEndpoint = new Endpoint(object);
            endpoints.add(newEndpoint);
        }
        } catch( IOException exception) {
            exception.printStackTrace();
        }
    }
}
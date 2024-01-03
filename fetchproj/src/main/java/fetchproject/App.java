package fetchproject;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;


/**
 * App to test availability of domains based on a configuration file written in YAML.
 * Takes user input of a path to the configuration file. Parses the YAML into Java objects (Endpoints).
 * Java objects are used to test availability to the endpoints in the YAML file.
 * Tests are run every 15 seconds and output is provided after every set of tests.
 * Verbosity can be enabled with the command line argument -v for more information on each test.
 */
public class App 
{
    static boolean verbose = false;
    //hashmap to store domain data. Key is the domain name, the array contains successful and total tests for each domain
    public static HashMap<String,double[]> urlValues = new HashMap<String,double[]>();
    public static void main( String[] args )
    {
        String pathName = null;
        if (args.length > 0 && (args[0] != null && !(args[0].equals("-v")))) {
            pathName = args[0];
        }
        if (args.length > 1 && (args[1] != null && (args[1].equals("-v")))) {
            verbose = true;
        }
        if (pathName == null) {
            System.out.println("please provide a pathname to YAML configuration");
            System.exit(1);
        }
        try { //Open the file provided by the user
            File configFile = new File(pathName); 
            String path = configFile.getPath(); 
            if(verbose) {
                System.out.println("Parsing Configuration");
            }
            ParseConfig.ParseYaml(path); //parse the YAML into Endpoint objects
            if(verbose) {
                System.out.println("Completed Parsing Configuration");
            }
        } catch (BadConfigurationException e) {
            System.err.println(e.getMessage());
            //will continue if configuration is bad
        } 
        catch (Exception e) { 
            System.err.println(e.getMessage());
            System.exit(1);
        }
        for (Endpoint endpoint : ParseConfig.endpoints) { //track domains of endpoints
            try {
                URI uri = new URI(endpoint.getUrl());
                String domain = uri.getHost();
                if(!urlValues.containsKey(domain)) { //domains are stored in a hashmap since we may have duplicates
                    urlValues.put(domain, new double[2]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //using while true since its specified tests are run until user inputs a hard stop
        if(verbose) {
                System.out.println("Beginning Health Checks");
            }
        while (true) {
            for (Endpoint endpoint : ParseConfig.endpoints) {
                try {
                    URI uri = new URI(endpoint.getUrl());
                    String domain = uri.getHost();
                    double[] arr = urlValues.get(domain);
                    if(HealthChecks.EndpointHealthCheck(endpoint,verbose) == true) { //run health test
                        arr[0]++; //increment successful tests value for the domain if successful
                    }
                    arr[1]++; //increment total tests value for the domain
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 
            //adding some additional new lines with verbosity to make core results more readable 
            if(verbose) {
            System.out.println();
            }
            //calculate current availability percentage for each domain and print
            for (Map.Entry<String,double[]> entry : urlValues.entrySet()) {
                double AvPercentage = ((entry.getValue()[0])/(entry.getValue()[1]) * 100);
                int roundPercentage = (int) Math.round(AvPercentage);
                System.out.println(entry.getKey() + " has " + roundPercentage + "% availability percentage");
            }
            if(verbose) {
            System.out.println();
            }
            try{ //sleep for 15 seconds before running tests again
                Thread.sleep(15000);
            } catch (Exception e) {

            }
        }
    } 
}

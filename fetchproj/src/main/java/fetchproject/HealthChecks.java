package fetchproject;

import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

/*
 * Class containing healthchecks for endpoints
 */
public class HealthChecks {
    
    /*
     * Runs a health on an endpoint based on the contents of the endpoint
     * returns true if the response code from the endpoint is a successful one
     * (200-299) and the response time is less than 500 ms. returns false otherwise
     * 
     * When verbosity is enabled, each endpoint will be printed, as well as it's
     * return code and a message if the request timed out
     */
    public static boolean EndpointHealthCheck(Endpoint endpoint, boolean verbose) {
        try{
            //create a JAVA URL object from the endpoint url entry
            //creating a URL from string is deprecated so we need
            //to create a URI first
            URL url = URI.create(endpoint.getUrl()).toURL();
            //create a HTTP connection object from the URL
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            //set the request method
            con.setRequestMethod(endpoint.getMethod());
            //this needs to be true to set the headers and body
            con.setDoOutput(true);
            //iterate through the headers and add them
            if(endpoint.getHeaders() != null) {
                for (Map.Entry<String,String> entry : endpoint.getHeaders().entrySet()) {
                    con.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            //add the body
            if(endpoint.getBody() != null) { 
                con.getOutputStream().write(endpoint.getBody().getBytes("UTF8"));
            }
            //set the time out to 500 ms
            con.setReadTimeout(500);
            //initiate request
            if(verbose) {
                System.out.println("Initiating connection to " + endpoint.getName() + " at " + endpoint.getUrl() +
                " with " + endpoint.getMethod() + " method " + " with the following headers: " + endpoint.getHeaders() +
                " and the following body: " + endpoint.getBody());
            }
            int status = con.getResponseCode();
            con.disconnect();
            //read response code
            if(status <= 299 && status >= 200) {
                if (verbose) {
                    System.out.println("request to " + endpoint.getName() + " was succcessful with return code " + status );
                }
                return true;
            } else {
                if (verbose) {
                    System.out.println("request to " + endpoint.getName() + " was unsucccessful with return code " + status );
                }
                return false;
            }
            //SocketTimeoutException is thrown if the timeout limit is reached
        } catch(SocketTimeoutException e) {
            if (verbose) {
                    System.out.println("request to " + endpoint.getName() + " was unsucccessful due to timeout");
                }
            return false;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

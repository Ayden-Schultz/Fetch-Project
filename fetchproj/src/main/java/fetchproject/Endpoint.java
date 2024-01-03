package fetchproject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/*
 * Class containing information about endpoints parsed out of the YAML configuration file
 */
public class Endpoint {
    private String name;
    private String url;
    private String method;
    private HashMap<String,String> headers;
    private String body;

    @SuppressWarnings("unchecked")
    public Endpoint(HashMap<String, Object> input) throws BadConfigurationException{
        //name and url are both required so we will throw an exception if either are not present
        this.name = (String) input.get("name");
        if (this.name == null) {
            throw new BadConfigurationException("Endpoint does not have a name");
        }
        this.url = (String) input.get("url");
        if (this.name == null) {
            throw new BadConfigurationException("Endpoint does not have a url");
        }
        //default method to GET
        this.method = (String) input.get("method");
        if (this.method == null) {
            this.method = "GET";
        }
        this.body = (String) input.get("body");
        Object headersObj = input.get("headers");
        //Since we are suppressing the unchecked cast warning, do some quick type checking
        if(headersObj != null && headersObj.getClass() == LinkedHashMap.class) {
            headers = (HashMap<String,String>) headersObj;
        }
    }

    //getters
    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public String getMethod() {
        return this.method;
    }

    public String getBody() {
        return this.body;
    }

    public HashMap<String,String> getHeaders() {
        return this.headers;
    }
}

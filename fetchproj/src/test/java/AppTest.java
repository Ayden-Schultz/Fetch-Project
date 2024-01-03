

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;

import org.junit.Test;

import fetchproject.BadConfigurationException;
import fetchproject.Endpoint;
import fetchproject.HealthChecks;
import fetchproject.ParseConfig;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Test parser with example YAML file
     */
    @Test
    public void yamlShouldParse()
    {
        File configFile = new File("src\\main\\resources\\input.yaml"); 
        String path = configFile.getPath();
        try{
        ParseConfig.ParseYaml(path);
        }catch(BadConfigurationException e) {
            fail("Exception thrown on configuration");
        } 
        assertEquals(4, ParseConfig.endpoints.size());
    }

    /*
     * test healthcheck with endpoint
     */
    @Test
    public void healthCheckShouldPass() {
         HashMap<String,Object> testMap = new HashMap<String, Object>() {{
        put("name", "rewards");
        put("url", "https://www.fetchrewards.com/");
        }};
        Endpoint testEndpoint = null;
        try {
        testEndpoint = new Endpoint(testMap);
        } catch(BadConfigurationException e) {
            fail("Exception thrown on configuration");
        }
        assertTrue(HealthChecks.EndpointHealthCheck(testEndpoint, false));
    }

    /*
     * test healthcheck with bad endpoint
     */
    @Test
    public void healthCheckShouldFail() {
         HashMap<String,Object> testMap = new HashMap<String, Object>() {{
        put("name", "bad");
        put("url", "https://www.google.com/yada");
        }};
        Endpoint testEndpoint = null;
        try {
        testEndpoint = new Endpoint(testMap);
        } catch(BadConfigurationException e) {
            fail("Exception thrown on configuration");
        }
        assertFalse(HealthChecks.EndpointHealthCheck(testEndpoint, false));
    }
}

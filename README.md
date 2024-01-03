# Fetch-Project

Author: Ayden Schultz

This project is built via Maven and JDK 21.

JDK 21 can be downloaded from here:
https://www.oracle.com/java/technologies/downloads/

instructions for installing and configuring Maven can be found here:
https://maven.apache.org/install.html

The project uses an open source library called SnakeYaml to handle the parsing of the YAML file to Java objects.

Once maven is set up, all that's needed to compile the package is run 'mvn package' from inside the fetchproj directory. This should package the project along with the SnakeYaml dependency. I've written some unit tests for the methods used for the program that will run during compilation.

I've included a compiled version of the code as well - fetchproj-1.0.jar . This can be run from the command line via java -jar fetchproj-1.0.jar [args]

The program excepts 2 arguments, the first argument must be the path of the yaml configuration file containing the endpoints to test. A second optional flag '-v' can be included to add some verbosity to the program. This will include extra printouts for test results contaning the return code of each endpoint.

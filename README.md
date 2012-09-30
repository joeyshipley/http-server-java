http-server-java
================

Simple HTTP server in java.

### Running the Server
java -jar http-server.jar -p 5000 -d public
  -p: is the port the server will run on.
  -d: is the relative directory to the http-server.jar file that the server will look for its content files from.

Passing in invalid parameters will default the server to port 5000 with a directory of "public".

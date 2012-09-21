package Core;

import java.io.IOException;

public class ServerRunner
{
    public static void main(String[] args)
        throws IOException
    {
        ServerRunnerArguments arguments = ServerRunnerArguments.createFrom(args);
        HttpServer httpServer = HttpServer.createFrom(arguments.Port, arguments.Directory);
        httpServer.start();
    }

    public static void log(String message)
    {
        System.out.println(message);
    }
}
package Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer
{
    private int _port;
    private String _directory;
    private ServerSocket _serverSocket;

    private HttpServer(int port, String directory)
    {
        _port = port;
        _directory = directory;
    }

    public static HttpServer createFrom(int port, String directory)
    {
        return new HttpServer(port, directory);
    }

    public void start()
        throws IOException
    {
        _serverSocket = new ServerSocket(_port);
        ServerRunner.log("HTTP Server on port: " + _port);
        while(true)
            createHttpServerProcessOnNewThread();
    }

    public void stop()
        throws IOException
    {
        _serverSocket.close();
    }

    private void createHttpServerProcessOnNewThread()
    {
        HttpServerProcess process = HttpServerProcess.createFrom(_serverSocket);
        Thread thread = new Thread(process);
        thread.run();
    }
}

package Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer
{
    private int port;
    private String directory;
    private ServerSocket serverSocket;

    private HttpServer(int port, String directory)
    {
        this.port = port;
        this.directory = directory;
    }

    public static HttpServer createFrom(int port, String directory)
    {
        return new HttpServer(port, directory);
    }

    public void start()
        throws IOException
    {
        serverSocket = new ServerSocket(port);
        ServerRunner.log("HTTP Server on port: " + port);
        while(true)
            createHttpServerProcessOnNewThread();
    }

    public void stop()
        throws IOException
    {
        serverSocket.close();
    }

    private void createHttpServerProcessOnNewThread()
    {
        HttpServerProcess process = HttpServerProcess.createFrom(serverSocket, directory);
        Thread thread = new Thread(process);
        thread.run();
    }
}

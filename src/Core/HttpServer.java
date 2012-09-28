package Core;

import Core.RequestResponse.RequestResponseHandler;

import java.io.IOException;
import java.net.ServerSocket;

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
            createRequestResponseHandlerOnNewThread();
    }

    public void stop()
        throws IOException
    {
        serverSocket.close();
    }

    private void createRequestResponseHandlerOnNewThread()
    {
        RequestResponseHandler handler = RequestResponseHandler.createFrom(serverSocket, directory);
        Thread thread = new Thread(handler);
        thread.run();
    }
}

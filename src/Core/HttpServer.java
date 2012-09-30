package Core;

import Core.RequestResponse.RequestResponseHandler;

import java.io.IOException;
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
        ServerRunner.log("CTRL-C to end it.\r\n");
        while(true)
            createRequestResponseHandlerOnNewThread();
    }

    public void stop()
        throws IOException
    {
        serverSocket.close();
    }

    private void createRequestResponseHandlerOnNewThread()
        throws IOException
    {
        Socket clientSocket = serverSocket.accept();
        RequestResponseHandler handler = RequestResponseHandler.createFrom(clientSocket, directory);
        Thread thread = new Thread(handler);
        thread.start();
    }
}

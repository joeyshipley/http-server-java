package Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServerProcess implements Runnable
{
    private ServerSocket _serverSocket;

    private HttpServerProcess(ServerSocket serverSocket)
    {
        _serverSocket = serverSocket;
    }

    public static HttpServerProcess createFrom(ServerSocket serverSocket)
    {
        return new HttpServerProcess(serverSocket);
    }

    public void run()
    {
        ServerRunner.log("CTRL-C to end it.\r\n");
        boolean continueListening = true;
        while(continueListening)
            manageCommunicationCycle();
    }

    public void manageCommunicationCycle()
    {
        try
        {
            Socket clientSocket = _serverSocket.accept();

            readRequest(clientSocket);
            sendResponse(clientSocket);

            clientSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void readRequest(Socket clientSocket)
        throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        while(true)
        {
            String requestString = in.readLine();
            ServerRunner.log(requestString);
            if(requestString.equals("")) break;
        }
    }

    private void sendResponse(Socket clientSocket)
        throws IOException
    {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("");
        out.println("<html>");
        out.println("<head><title>HTTP Server: Hello World</title></head>");
        out.println("<body><p>Hello World</p></body>");
        out.println("</html>");
        out.flush();
    }
}

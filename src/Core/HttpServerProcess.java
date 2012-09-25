package Core;

import java.io.*;
import java.net.*;

public class HttpServerProcess implements Runnable
{
    private ServerSocket server;
    private RequestParser requestParser;
    private ResponseProvider responseProvider;

    private HttpServerProcess(ServerSocket serverSocket)
    {
        server = serverSocket;
        requestParser = RequestParser.create();
        responseProvider = ResponseProvider.create();
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
            Socket clientSocket = server.accept();
            RequestInformation info = readRequest(clientSocket);
            sendResponse(clientSocket, info.method, info.path);
            clientSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private RequestInformation readRequest(Socket clientSocket)
        throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String requestHeader = "";

        boolean continueReading = true;
        while(continueReading)
        {
            String currentRequestLine = in.readLine();
            ServerRunner.log(currentRequestLine);

            if(requestHeader.equals(""))
                requestHeader = currentRequestLine;

            if(isEndOfRequestHeader(currentRequestLine))
                continueReading = false;
        }
        return requestParser.buildHeaderInfo(requestHeader);
    }

    private boolean isEndOfRequestHeader(String currentHeaderLineValue)
    {
        return currentHeaderLineValue.equals("");
    }

    private void sendResponse(Socket clientSocket, String method, String path)
        throws IOException
    {
        String[] response = responseProvider.getResponseFrom(method, path);

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
        for(int i = 0; i < response.length; i++)
            out.println(response[i]);
        out.flush();
    }
}

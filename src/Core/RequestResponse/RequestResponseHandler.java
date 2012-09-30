package Core.RequestResponse;

import Core.ServerRunner;

import java.io.*;
import java.net.*;

public class RequestResponseHandler implements Runnable
{
    private Socket clientSocket;
    private RequestParser requestParser;
    private ResponseProvider responseProvider;

    private RequestResponseHandler(Socket clientSocket, String rootDirectory)
    {
        this.clientSocket = clientSocket;
        requestParser = RequestParser.create();
        responseProvider = ResponseProvider.create(rootDirectory);
    }

    public static RequestResponseHandler createFrom(Socket clientSocket, String rootDirectory)
    {
        return new RequestResponseHandler(clientSocket, rootDirectory);
    }

    public void run()
    {
        manageCommunicationCycle();
    }

    public void manageCommunicationCycle()
    {
        ServerRunner.log("Processing on thread: " + Thread.currentThread().getName());
        try
        {
            RequestInformation info = readRequest(clientSocket);
            sendResponse(clientSocket, info);
            pauseForInputReading();
            clientSocket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private RequestInformation readRequest(Socket clientSocket)
        throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String requestString = "";
        boolean continueReading = true;
        while(continueReading)
        {
            String currentRequestLine = in.readLine();
            ServerRunner.log("request line: " + currentRequestLine);
            requestString += currentRequestLine + "\r\n";

            if(isEndOfRequestHeader(currentRequestLine))
                continueReading = false;
        }

        requestString += gatherFormDataIfAvailable(in);

        return requestParser.parseInfoFromRequest(requestString);
    }

    private String gatherFormDataIfAvailable(BufferedReader in)
        throws IOException
    {
        if(!in.ready())
            return "";

        StringBuffer buffer = new StringBuffer();
        while (in.ready()) {
            int ch = in.read();
            buffer.append((char) ch);
        }
        return buffer.toString();
    }

    private boolean isEndOfRequestHeader(String currentHeaderLineValue)
    {
        return currentHeaderLineValue.equals("");
    }

    private void sendResponse(Socket clientSocket, RequestInformation request)
        throws IOException
    {
        String[] response = responseProvider.getResponseFrom(request);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
        for(int i = 0; i < response.length; i++)
            out.println(response[i]);
        out.flush();
    }

    private void pauseForInputReading()
        throws InterruptedException
    {
        Thread.sleep(1L);
    }
}

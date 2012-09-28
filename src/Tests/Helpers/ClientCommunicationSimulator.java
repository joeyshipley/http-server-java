package Tests.Helpers;

import java.io.*;
import java.net.Socket;

public class ClientCommunicationSimulator
{
    private static String requestHeader;
    private static Socket clientSocket;
    private static String postData;

    public static void sendRequest(Socket client, String httpMethod, String path, String data)
        throws IOException
    {
        requestHeader = httpMethod + " " + path + " HTTP/1.0\r\n";
        System.out.println(requestHeader);
        clientSocket = client;
        postData = data;

        Thread thread = new Thread(new Runnable() {
            public void run()
            {
                try
                {
                    BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF8"));
                    wr.write(requestHeader);
                    wr.write("");
                    if(postData != null && postData != "") {
                        wr.write(postData);
                    }
                    wr.flush();

                    BufferedReader rd = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String response = "";
                    while(true)
                    {
                        response += rd.readLine();
                        System.out.println(response);
                        if(response.equals("")) break;
                    }

                    wr.close();
                    rd.close();
                } catch (Exception e) {}
            }
        });
    }
}

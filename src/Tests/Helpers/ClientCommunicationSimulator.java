package Tests.Helpers;

import java.io.*;
import java.net.Socket;

public class ClientCommunicationSimulator
{
    private static String _requestHeader;
    private static Socket _clientSocket;

    public static void sendRequest(Socket client, String httpMethod, String path, String data)
        throws IOException
    {
        _clientSocket = client;
        _requestHeader = httpMethod + " " + path + " HTTP/1.0\r\n";
        System.out.println(_requestHeader);

        Thread thread = new Thread(new Runnable() {
            public void run()
            {
                try
                {
                    BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(_clientSocket.getOutputStream(), "UTF8"));
                    wr.write(_requestHeader);
                    wr.write("");
                    //if(data != null && data != "") {
                    //    wr.write(data);
                    //}
                    wr.flush();

                    BufferedReader rd = new BufferedReader(new InputStreamReader(_clientSocket.getInputStream()));
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

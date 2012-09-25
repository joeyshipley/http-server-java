package Core;

import java.io.*;

public class ResponseProvider
{
    private ResponseProvider() {}

    public static ResponseProvider create()
    {
        return new ResponseProvider();
    }

    public String[] getResponseFrom(String method, String path)
    {
        return loadFileContents(path);
    }

    public String[] loadFileContents(String filePath)
    {
        String[] response = new String[0];
        String path = getActualPath(filePath);

        if(!fileExists(path))
            return notFoundResponseHeaders().split("\r\n");

        try
        {
            FileInputStream inputStream = new FileInputStream(path);
            DataInputStream dataStream = new DataInputStream(inputStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(dataStream));

            String fileResponse = "";
            fileResponse += successfulResponseHeaders();

            boolean continueReadingFile = true;
            while (continueReadingFile)
            {
                String currentLine = reader.readLine();
                if(currentLine != null)
                    fileResponse += currentLine + "\r\n";
                else
                    continueReadingFile = false;
            }
            response = fileResponse.split("\r\n");

            reader.close();
            dataStream.close();
            inputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return response;
    }

    public String getActualPath(String filePath)
    {
        if(filePath == null)
            filePath = "";
        if(!filePath.startsWith("/"))
            filePath = "/" + filePath;

        String path;
        if(filePath.equals("/"))
            path = "/public/default.html";
        else
            path = "/public" + filePath;

        return "." + path;
    }

    private boolean fileExists(String path)
    {
        try
        {
            FileInputStream inputStream = new FileInputStream(path);
            inputStream.close();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    private String successfulResponseHeaders()
    {
        String headers = "";
        headers += "HTTP/1.1 200 OK\r\n";
        headers += "Content-Type: text/html\r\n";
        headers += "\r\n";
        return headers;
    }

    private String notFoundResponseHeaders()
    {
        String headers = "";
        headers += "HTTP/1.1 404 Not Found\r\n";
        headers += "\r\n";
        return headers;
    }
}

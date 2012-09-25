package Core;

import java.io.*;

public class ResponseProvider
{
    private String directory;

    private ResponseProvider(String rootDirectory)
    {
        directory = rootDirectory;
    }

    public static ResponseProvider create(String rootDirectory)
    {
        return new ResponseProvider(rootDirectory);
    }

    public String[] getResponseFrom(String method, String path)
    {
        path = getActualPath(path);

        if(isDirectory(path))
            return loadDirectoryContents(path);

        return loadFileContents(path);
    }

    private String[] loadDirectoryContents(String path)
    {
        File directory = new File(path);
        String[] files = directory.list();

        String responseString = "";
        responseString += successfulResponseHeaders();
        responseString += buildHtmlPageStart();
        for(int i = 0; i < files.length; i++)
            responseString += convertFileToHtmlLink(files[i]) + "\r\n";
        responseString += buildHtmlPageEnd();

        return responseString.split("\r\n");
    }

    private String[] loadFileContents(String path)
    {
        if(!fileExists(path))
            return notFoundResponseHeaders().split("\r\n");

        String[] response = new String[0];
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
        if(hasTrailingSlash(filePath))
            filePath = filePath.substring(0, filePath.length() - 1);
        if(shouldPathHaveLeadingSlash(filePath))
            filePath = "/" + filePath;

        String path;
        path = "./" + directory;

        if(isDefaultDirectory(filePath))
            return path + "/";

        if(isPathRootPath(filePath))
            return path + "/default.html";

        return path + filePath;
    }

    private String buildHtmlPageStart()
    {
        String page = "";
        page += "<html>\r\n";
        page += "<head></head>\r\n";
        page += "<body>\r\n";
        return page;
    }

    private String buildHtmlPageEnd()
    {
        String page = "";
        page += "</body>\r\n";
        page += "</html>\r\n";
        return page;
    }

    private String convertFileToHtmlLink(String filename)
    {
        return "<a href='" + filename + "'>" + filename + "</a><br />";
    }

    private boolean isDefaultDirectory(String path)
    {
        return path.equals("/")
            || path.toLowerCase().equals("/" + directory.toLowerCase());
    }

    private boolean isPathRootPath(String path)
    {
        return path.equals("");
    }

    private boolean shouldPathHaveLeadingSlash(String path)
    {
        return !path.startsWith("/") && path.length() > 1;
    }

    private boolean hasTrailingSlash(String path)
    {
        if(path.length() < 2)
            return false;

        return (path.length() - 1) == path.lastIndexOf("/");
    }

    private boolean fileExists(String path)
    {
        File file = new File(path);
        return file.exists();
    }

    private boolean isDirectory(String path)
    {
        File file = new File(path);
        return file.exists() && file.isDirectory();
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

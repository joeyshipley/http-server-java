package Core;

import Core.Helpers.UrlHelper;
import Core.PageParsers.DefaultPageParser;
import Core.PageParsers.ShipwreckServerPageParser;
import Core.PageParsers.SingleLineParser;

import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class ResponseProvider
{
    private String directory;
    private DefaultPageParser defaultParser;
    private ShipwreckServerPageParser shipwreckParser;

    private ResponseProvider(String rootDirectory)
    {
        directory = rootDirectory;
        defaultParser = DefaultPageParser.create();
        shipwreckParser = ShipwreckServerPageParser.create();
    }

    public static ResponseProvider create(String rootDirectory)
    {
        return new ResponseProvider(rootDirectory);
    }

    public String[] getResponseFrom(String method, String path)
    {
        String actualPath = getActualPath(path);

        if(isDirectory(actualPath))
            return loadDirectoryContents(actualPath);

        List<AbstractMap.SimpleEntry<String, String>> queryStringValues = UrlHelper.parseQuerystringValuesFrom(path);
        return loadFileContents(actualPath, queryStringValues);
    }

    public String getActualPath(String filePath)
    {
        if(filePath == null)
            filePath = "";

        filePath = removeQuerystringValues(filePath);

        if(hasTrailingSlash(filePath))
            filePath = filePath.substring(0, filePath.length() - 1);
        if(shouldPathHaveLeadingSlash(filePath))
            filePath = "/" + filePath;

        String path;
        path = "./" + directory;

        if(isDefaultDirectory(filePath))
            return path + "/";

        if(isPathRootPath(filePath))
            return path + "/default.shipwreck";

        if(isShipwreckPageWithoutExtension(filePath))
            return path + filePath + ".shipwreck";

        return path + filePath;
    }

    private String removeQuerystringValues(String path)
    {
        int indexOfQuestionMark = path.indexOf("?");
        if(indexOfQuestionMark > 0)
            path = path.substring(0, indexOfQuestionMark);
        return path;
    }

    private String[] loadDirectoryContents(String path)
    {
        File directory = new File(path);
        String[] files = directory.list();
        String responseString = buildDirectoryResponse(files);
        return responseString.split("\r\n");
    }

    private String[] loadFileContents(String path, List<AbstractMap.SimpleEntry<String, String>> data)
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
            SingleLineParser parser = isShipwreckServerPage(path) ? shipwreckParser : defaultParser;
            fileResponse = buildFileContentsResponse(reader, parser, data);
            fileResponse = successfulResponseHeaders() + fileResponse;
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

    private String buildFileContentsResponse(BufferedReader reader, SingleLineParser parser, List<AbstractMap.SimpleEntry<String, String>> data)
        throws IOException
    {
        String fileResponse = "";
        String currentLine;
        while ((currentLine = reader.readLine()) != null)
            fileResponse += parser.parseSingleLine(currentLine, data) + "\r\n";
        return fileResponse;
    }

    private String buildDirectoryResponse(String[] files)
    {
        String responseString = "";
        responseString += successfulResponseHeaders();
        responseString += buildHtmlPageStart();
        for(int i = 0; i < files.length; i++)
            responseString += convertFileToHtmlLink(files[i]) + "\r\n";
        responseString += buildHtmlPageEnd();
        return responseString;
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

    private boolean isShipwreckPageWithoutExtension(String path)
    {
        String[] pathParts = path.split("/");
        if(pathParts.length == 0)
            return false;

        String filename = pathParts[pathParts.length - 1];
        return hasFileExtension(filename);
    }

    private boolean hasFileExtension(String filename)
    {
        return filename.indexOf(".") == -1;
    }

    private boolean isShipwreckServerPage(String path)
    {
        return path.indexOf(".shipwreck") > -1;
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

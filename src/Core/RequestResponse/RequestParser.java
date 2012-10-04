package Core.RequestResponse;

import Core.ServerRunner;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class RequestParser
{
    private RequestParser() {}

    public static RequestParser create()
    {
        return new RequestParser();
    }

    public RequestInformation parseInfoFromRequest(String inRequest)
    {
        String[] request = inRequest.split("\r\n");
        if(request.length == 0)
            return RequestInformation.createEmpty();

        RequestInformation info = buildInfoFromHeader(getHeaderValueFrom(request));
        populateQueryStringData(info);
        populateFormData(info, request);

        return info;
    }

    private String getHeaderValueFrom(String[] request)
    {
        return request[0];
    }

    private void populateQueryStringData(RequestInformation info)
    {
        info.data.addAll(parseQuerystringValuesFrom(info.path));
    }

    private void populateFormData(RequestInformation info, String[] request)
    {
        String data = getDataStringFromRequest(request);
        if(isActuallyDataStringAndNotARequestHeaderValue(data))
            data = "";
        info.data.addAll(parseDataFrom(data));
    }

    private String getDataStringFromRequest(String[] request)
    {
        return request[request.length - 1];
    }

    private boolean isActuallyDataStringAndNotARequestHeaderValue(String lineInQuestion)
    {
        // NOTE: the idea that data won't contain ": " sucks as a logic check,
        // but works for the constraints of the project & time.
        return lineInQuestion.contains(": ");
    }

    public RequestInformation buildInfoFromHeader(String header)
    {
        String[] headerParts = header.split(" ");
        String method = getMethodFromHeaderParts(headerParts);
        String path = getPathFromHeaderParts(headerParts);
        boolean isTttRoute = confirmIfIsTicTacToeGame(headerParts);

        return RequestInformation.create(method, path, isTttRoute);
    }

    private boolean confirmIfIsTicTacToeGame(String[] headerParts)
    {
        if(headerParts.length < 1)
            return false;

        boolean isTtt = false;
        for(int i = 0; i < headerParts.length; i++)
            if(headerParts[i].contains("tictactoe"))
                isTtt = true;

        return isTtt;
    }

    private String getMethodFromHeaderParts(String[] headerParts)
    {
        if(headerParts.length < 1)
            return "";

        return headerParts[0];
    }

    private String getPathFromHeaderParts(String[] headerParts)
    {
        if(headerParts.length < 2)
            return "";

        return headerParts[1];
    }

    public List<AbstractMap.SimpleEntry<String, String>> parseQuerystringValuesFrom(String url)
    {
        if(!hasQuerystringValues(url))
            return new ArrayList<AbstractMap.SimpleEntry<String, String>>();

        String querystring = url.substring(url.indexOf("?") + 1, url.length());
        return parseDataFrom(querystring);
    }

    private boolean hasQuerystringValues(String url)
    {
        return url != null && url.contains("?") && url.indexOf("?") != url.length();
    }

    private List<AbstractMap.SimpleEntry<String, String>> parseDataFrom(String data)
    {
        List<AbstractMap.SimpleEntry<String, String>> dataValues = new ArrayList<AbstractMap.SimpleEntry<String, String>>();

        String[] keyvaluepairs = data.split("&");
        for(int i = 0; i < keyvaluepairs.length; i++)
        {
            String kvp = keyvaluepairs[i];
            int indexOfEquals = kvp.indexOf("=");
            if(indexOfEquals < 0)
                continue;

            String key = kvp.substring(0, indexOfEquals);
            String value = kvp.substring(indexOfEquals, kvp.length()).replace("=", "");
            value = replaceEncodedSpacesWithActualSpaces(value);

            dataValues.add(new AbstractMap.SimpleEntry<String, String>(key, value));
        }
        return dataValues;
    }

    private String replaceEncodedSpacesWithActualSpaces(String value)
    {
        return value.replace("%20", " ");
    }
}

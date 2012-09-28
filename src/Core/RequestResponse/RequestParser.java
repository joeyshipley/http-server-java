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
            return RequestInformation.create("", "");

        RequestInformation info = buildHeaderInfo(getHeaderValueFrom(request));
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
        String data = request[request.length - 1];
        if(data.contains(": "))
            data = "";
        info.data.addAll(parseDataFrom(data));
    }

    public RequestInformation buildHeaderInfo(String header)
    {
        String[] headerParts = header.split(" ");
        if(header.length() < 2)
            return RequestInformation.create(headerParts[0], "");

        return RequestInformation.create(headerParts[0], headerParts[1]);
    }

    public List<AbstractMap.SimpleEntry<String, String>> parseQuerystringValuesFrom(String url)
    {
        if(url == null || (!url.contains("?") && url.indexOf("?") != url.length()))
            return new ArrayList<AbstractMap.SimpleEntry<String, String>>();

        String querystring = url.substring(url.indexOf("?") + 1, url.length());
        return parseDataFrom(querystring);
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

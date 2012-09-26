package Core.Helpers;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class UrlHelper
{
    public static List<AbstractMap.SimpleEntry<String, String>> parseQuerystringValuesFrom(String url)
    {
        List<AbstractMap.SimpleEntry<String, String>> querystringValues = new ArrayList<AbstractMap.SimpleEntry<String, String>>();

        if(!url.contains("?") && url.indexOf("?") != url.length())
            return querystringValues;

        String querystring = url.substring(url.indexOf("?") + 1, url.length());
        String[] keyvaluepairs = querystring.split("&");
        for(int i = 0; i < keyvaluepairs.length; i++)
        {
            String kvp = keyvaluepairs[i];
            int indexOfEquals = kvp.indexOf("=");
            if(indexOfEquals < 0)
                continue;

            String key = kvp.substring(0, indexOfEquals);
            String value = kvp.substring(indexOfEquals, kvp.length()).replace("=", "");
            value = replaceEncodedSpacesWithActualSpaces(value);

            querystringValues.add(new AbstractMap.SimpleEntry<String, String>(key, value));
        }

        return querystringValues;
    }

    private static String replaceEncodedSpacesWithActualSpaces(String value)
    {
        return value.replace("%20", " ");
    }
}

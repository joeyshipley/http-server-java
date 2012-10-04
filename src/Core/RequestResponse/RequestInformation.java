package Core.RequestResponse;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class RequestInformation
{
    public String method;
    public String path;
    public List<AbstractMap.SimpleEntry<String, String>> data;
    public boolean isConsoleGameRoute;

    private RequestInformation(String method, String path, List<AbstractMap.SimpleEntry<String, String>> data, boolean isConsoleGameRoute)
    {
        this.method = method;
        this.path = path;
        this.data = data;
        this.isConsoleGameRoute = isConsoleGameRoute;
    }

    public static RequestInformation create(String method, String path)
    {
        return new RequestInformation(method, path, new ArrayList<AbstractMap.SimpleEntry<String, String>>(), false);
    }

    public static RequestInformation create(String method, String path, List<AbstractMap.SimpleEntry<String, String>> data)
    {
        return new RequestInformation(method, path, data, false);
    }

    public static RequestInformation create(String method, String path, boolean isConsoleGameRoute)
    {
        return new RequestInformation(method, path, new ArrayList<AbstractMap.SimpleEntry<String, String>>(), isConsoleGameRoute);
    }

    public static RequestInformation create(String method, String path, List<AbstractMap.SimpleEntry<String, String>> data, boolean isConsoleGameRoute)
    {
        return new RequestInformation(method, path, data, isConsoleGameRoute);
    }

    public static RequestInformation createEmpty()
    {
        return RequestInformation.create("", "");
    }
}

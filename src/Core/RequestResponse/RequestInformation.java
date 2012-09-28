package Core.RequestResponse;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class RequestInformation
{
    public String method;
    public String path;
    public List<AbstractMap.SimpleEntry<String, String>> data;

    private RequestInformation(String method, String path, List<AbstractMap.SimpleEntry<String, String>> data)
    {
        this.method = method;
        this.path = path;
        this.data = data;
    }

    public static RequestInformation create(String method, String path)
    {
        return new RequestInformation(method, path, new ArrayList<AbstractMap.SimpleEntry<String, String>>());
    }

    public static RequestInformation create(String method, String path, List<AbstractMap.SimpleEntry<String, String>> data)
    {
        return new RequestInformation(method, path, data);
    }

    public static RequestInformation createEmpty()
    {
        return RequestInformation.create("", "");
    }
}

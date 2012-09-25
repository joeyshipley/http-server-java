package Core;

public class RequestParser
{
    private RequestParser() {}

    public static RequestParser create()
    {
        return new RequestParser();
    }

    public RequestInformation buildHeaderInfo(String header)
    {
        String[] headerParts = header.split(" ");
        RequestInformation info = new RequestInformation();
        info.method = headerParts[0];
        info.path = headerParts[1];
        return info;
    }
}

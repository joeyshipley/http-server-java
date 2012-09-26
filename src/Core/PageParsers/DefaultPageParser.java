package Core.PageParsers;


import java.util.AbstractMap;
import java.util.List;

public class DefaultPageParser implements SingleLineParser
{
    private DefaultPageParser() {}

    public static DefaultPageParser create()
    {
        return new DefaultPageParser();
    }

    public String parseSingleLine(String currentLine, List<AbstractMap.SimpleEntry<String, String>> values)
    {
        return currentLine;
    }
}

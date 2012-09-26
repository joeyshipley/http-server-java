package Core.PageParsers;

import java.util.AbstractMap;
import java.util.List;

public interface SingleLineParser
{
    String parseSingleLine(String currentLine, List<AbstractMap.SimpleEntry<String, String>> values);
}

package Tests;

import Core.PageParsers.*;
import org.junit.*;
import java.util.*;
import static junit.framework.Assert.*;

public class ShipwreckServerPageParserTests
{
    private SingleLineParser SUT;

    @Before
    public void each()
    {
        SUT = ShipwreckServerPageParser.create();
    }

    @Test
    public void when_using_the_parser___and_the_line_is_standard_html___it_should_return_the_line_as_is()
    {
        String result = SUT.parseSingleLine("<div>test</div>", null);
        assertEquals("<div>test</div>", result);
    }

    @Test
    public void when_using_the_parser___and_the_line_has_a_processing_tag___it_should_return_the_line_as_is()
    {
        List<AbstractMap.SimpleEntry<String, String>> values = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
        values.add(new AbstractMap.SimpleEntry<String, String>("my_val", "success"));

        String result = SUT.parseSingleLine("<div>test: <^= my_val ^></div>", values);
        assertEquals("<div>test: success</div>", result);
    }

    @Test
    public void when_using_the_parser___and_the_line_has_more_than_one_processing_tag___it_should_return_the_line_as_is()
    {
        List<AbstractMap.SimpleEntry<String, String>> values = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
        values.add(new AbstractMap.SimpleEntry<String, String>("my_val_1", "success 1"));
        values.add(new AbstractMap.SimpleEntry<String, String>("my_val_2", "success 2"));

        String result = SUT.parseSingleLine("<div>test: <^= my_val_1 ^>, <^=my_val_2^></div>", values);
        assertEquals("<div>test: success 1, success 2</div>", result);
    }
}


package Tests;

import Core.PageParsers.DefaultPageParser;
import Core.PageParsers.SingleLineParser;
import org.junit.*;
import static junit.framework.Assert.*;

public class DefaultPageParserTests
{
    private SingleLineParser SUT;

    @Before
    public void each()
    {
        SUT = DefaultPageParser.create();
    }

    @Test
    public void when_using_the_default_parser___it_should_return_the_line_as_is()
    {
        String result = SUT.parseSingleLine("<div>test</div>", null);
        assertEquals("<div>test</div>", result);
    }
}

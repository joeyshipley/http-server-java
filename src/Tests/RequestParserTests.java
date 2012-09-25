package Tests;

import Core.RequestParser;
import static junit.framework.Assert.*;
import org.junit.*;

public class RequestParserTests
{
    @Test
    public void when_askings_for_the_request_method_of_a_request_that_is_a_GET()
    {
        RequestParser SUT = RequestParser.create();
        String method = SUT.buildHeaderInfo("GET /example/file.html HTTP/1.1").method;
        assertEquals("GET", method);
    }

    @Test
    public void when_askings_for_the_request_method_of_a_request_that_is_a_POST()
    {
        RequestParser SUT = RequestParser.create();
        String method = SUT.buildHeaderInfo("POST /example/file.html HTTP/1.1").method;
        assertEquals("POST", method);
    }

    @Test
    public void when_askings_for_the_request_method_of_a_request_that_is_a_PUT()
    {
        RequestParser SUT = RequestParser.create();
        String method = SUT.buildHeaderInfo("PUT /example/file.html HTTP/1.1").method;
        assertEquals("PUT", method);
    }

    @Test
    public void when_askings_for_the_request_path_of_a_request_that_points_to_the_root()
    {
        RequestParser SUT = RequestParser.create();
        String path = SUT.buildHeaderInfo("PUT / HTTP/1.1").path;
        assertEquals("/", path);
    }

    @Test
    public void when_askings_for_the_request_path_of_a_request_that_points_to_somewhere_else()
    {
        RequestParser SUT = RequestParser.create();
        String path = SUT.buildHeaderInfo("PUT /somewhere/else HTTP/1.1").path;
        assertEquals("/somewhere/else", path);
    }
}

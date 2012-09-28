package Tests;

import Core.RequestResponse.RequestInformation;
import Core.RequestResponse.RequestParser;
import static junit.framework.Assert.*;
import org.junit.*;
import java.util.AbstractMap;
import java.util.List;

public class RequestParserTests
{
    private RequestParser SUT;

    @Before
    public void each()
    {
        SUT = RequestParser.create();
    }

    @Test
    public void when_asking_for_the_request_information_of_a_GET_request()
    {
        String inRequest = "";
        inRequest += "GET /default HTTP/1.1\r\n";
        inRequest += "Host: localhost:5000\r\n";
        inRequest += "Connection: keep-alive\r\n";
        inRequest += "Cache-Control: max-age=0\r\n";
        inRequest += "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/537.4\r\n";
        inRequest += "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n";
        inRequest += "Accept-Encoding: gzip,deflate,sdch\r\n";
        inRequest += "Accept-Language: en-US,en;q=0.8\r\n";
        inRequest += "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.3\r\n";
        inRequest += "\r\n";

        RequestInformation info = SUT.parseInfoFromRequest(inRequest);
        assertEquals("GET", info.method);
        assertEquals("/default", info.path);
    }

    @Test
    public void when_asking_for_the_request_information_of_a_GET_with_querystring_values_request()
    {
        String inRequest = "";
        inRequest += "GET /default?rawr=true HTTP/1.1\r\n";
        inRequest += "Host: localhost:5000\r\n";
        inRequest += "Connection: keep-alive\r\n";
        inRequest += "Cache-Control: max-age=0\r\n";
        inRequest += "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/537.4\r\n";
        inRequest += "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n";
        inRequest += "Accept-Encoding: gzip,deflate,sdch\r\n";
        inRequest += "Accept-Language: en-US,en;q=0.8\r\n";
        inRequest += "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.3\r\n";
        inRequest += "\r\n";

        RequestInformation info = SUT.parseInfoFromRequest(inRequest);
        assertEquals("rawr", info.data.get(0).getKey());
        assertEquals("true", info.data.get(0).getValue());
    }

    @Test
    public void when_asking_for_the_request_information_of_a_POST_request()
    {
        String inRequest = "";
        inRequest += "POST /form HTTP/1.1\r\n";
        inRequest += "Host: localhost:5000\r\n";
        inRequest += "Connection: keep-alive\r\n";
        inRequest += "Content-Length: 27\r\n";
        inRequest += "Cache-Control: max-age=0\r\n";
        inRequest += "Origin: http://localhost:5000\r\n";
        inRequest += "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/537.4\r\n";
        inRequest += "Content-Type: application/x-www-form-urlencoded\r\n";
        inRequest += "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n";
        inRequest += "Referer: http://localhost:5000/default\r\n";
        inRequest += "Accept-Encoding: gzip,deflate,sdch\r\n";
        inRequest += "Accept-Language: en-US,en;q=0.8\r\n";
        inRequest += "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.3\r\n";
        inRequest += "\r\n";
        inRequest += "My=Example&post-button=POST\r\n";
        inRequest += "\r\n";

        RequestInformation info = SUT.parseInfoFromRequest(inRequest);
        assertEquals("POST", info.method);
        assertEquals("/form", info.path);
        assertEquals("My", info.data.get(0).getKey());
        assertEquals("Example", info.data.get(0).getValue());
    }

    @Test
    public void when_askings_for_the_request_method_of_a_request_that_is_a_GET()
    {
        String method = SUT.buildHeaderInfo("GET /example/file.html HTTP/1.1").method;
        assertEquals("GET", method);
    }

    @Test
    public void when_askings_for_the_request_method_of_a_request_that_is_a_POST()
    {
        String method = SUT.buildHeaderInfo("POST /example/file.html HTTP/1.1").method;
        assertEquals("POST", method);
    }

    @Test
    public void when_askings_for_the_request_method_of_a_request_that_is_a_PUT()
    {
        String method = SUT.buildHeaderInfo("PUT /example/file.html HTTP/1.1").method;
        assertEquals("PUT", method);
    }

    @Test
    public void when_askings_for_the_request_path_of_a_request_that_points_to_the_root()
    {
        String path = SUT.buildHeaderInfo("PUT / HTTP/1.1").path;
        assertEquals("/", path);
    }

    @Test
    public void when_askings_for_the_request_path_of_a_request_that_points_to_somewhere_else()
    {
        String path = SUT.buildHeaderInfo("PUT /somewhere/else HTTP/1.1").path;
        assertEquals("/somewhere/else", path);
    }

    @Test
    public void when_asking_for_querystring_values_from_a_url_without_them___it_returns_an_empty_list()
    {
        List<AbstractMap.SimpleEntry<String, String>> results = SUT.parseQuerystringValuesFrom("mypath.html?");
        assertEquals(0, results.size());
    }

    @Test
    public void when_asking_for_querystring_values_from_a_url___it_returns_a_list_of_1_value()
    {
        List<AbstractMap.SimpleEntry<String, String>> results = SUT.parseQuerystringValuesFrom("mypath.html?val1=one");
        assertEquals(1, results.size());
        assertEquals("val1", results.get(0).getKey());
        assertEquals("one", results.get(0).getValue());
    }

    @Test
    public void when_asking_for_querystring_values_from_a_url___it_returns_a_list_of_2_values()
    {
        List<AbstractMap.SimpleEntry<String, String>> results = SUT.parseQuerystringValuesFrom("mypath.html?val1=one&val2=two");
        assertEquals(2, results.size());
    }

    @Test
    public void when_asking_for_querystring_values_from_a_url___it_returns_the_correctly_mapped_querystring_key_and_value()
    {
        List<AbstractMap.SimpleEntry<String, String>> results = SUT.parseQuerystringValuesFrom("mypath.html?val1=one");
        assertEquals("val1", results.get(0).getKey());
        assertEquals("one", results.get(0).getValue());
    }

    @Test
    public void when_asking_for_querystring_values_from_an_actual_url___it_returns_the_correct_values()
    {
        String url = "http://localhost:5000/some-script-url.shipwreck?variable_1=my%20value%20of%201&variable_2=yet%20another%20value%20of%20something";
        List<AbstractMap.SimpleEntry<String, String>> results = SUT.parseQuerystringValuesFrom(url);

        assertEquals("variable_1", results.get(0).getKey());
        assertEquals("my value of 1", results.get(0).getValue());

        assertEquals("variable_2", results.get(1).getKey());
        assertEquals("yet another value of something", results.get(1).getValue());
    }
}

package Tests;

import Core.ResponseProvider;
import org.junit.*;
import static junit.framework.Assert.*;

public class ResponseProviderTests
{
    private ResponseProvider SUT;

    @Before
    public void each()
    {
        SUT = ResponseProvider.create();
    }

    @Test
    public void when_asking_for_the_path___and_nothing_was_passed_in___it_returns_the_default_web_page()
    {
        String path = SUT.getActualPath("");
        assertEquals("./public/default.html", path);
    }

    @Test
    public void when_asking_for_the_path___and_the_root_was_passed_in___it_returns_the_default_web_page()
    {
        String path = SUT.getActualPath("/");
        assertEquals("./public/default.html", path);
    }

    @Test
    public void when_asking_for_the_path___and_they_are_asking_for_a_page___it_returns_the_page()
    {
        String path = SUT.getActualPath("/default.html");
        assertEquals("./public/default.html", path);
    }

    @Test
    public void when_asking_for_the_path___and_something_was_passed_in___it_returns_the_root_path()
    {
        String path = SUT.getActualPath("/some/thing");
        assertEquals("./public/some/thing", path);
    }

    @Test
    public void when_asking_for_the_path___and_something_was_passed_in_with_an_incomplete_path___it_returns_the_root_path()
    {
        String path = SUT.getActualPath("some/thing");
        assertEquals("./public/some/thing", path);
    }

    @Test
    public void when_asking_for_the_root_response___it_returns_hello_world()
    {
        String[] response = SUT.getResponseFrom("GET", "/");
        boolean hasHelloWorld = false;
        for(int i = 0; i < response.length; i++)
            if(response[i].toLowerCase().contains("hello world")) hasHelloWorld = true;

        assertTrue(hasHelloWorld);
    }

    @Test
    public void when_asking_for_a_response_that_is_a_valid_response___it_returns_a_200_OK_in_the_results()
    {
        String[] response = SUT.getResponseFrom("GET", "/");
        boolean has200OK = false;
        for(int i = 0; i < response.length; i++)
            if(response[i].toLowerCase().contains("http/1.1 200 ok")) has200OK = true;

        assertTrue(has200OK);
    }

    @Test
    public void when_asking_for_a_response_that_is_not_valid___it_returns_a_404_result()
    {
        String[] response = SUT.getResponseFrom("GET", "/404/doesnt/exist");
        boolean has404 = false;
        for(int i = 0; i < response.length; i++)
            if(response[i].toLowerCase().contains("http/1.1 404 not found")) has404 = true;

        assertTrue(has404);
    }
}
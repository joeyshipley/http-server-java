package Tests;

import Core.Helpers.UrlHelper;
import org.junit.Test;
import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;

import java.util.AbstractMap;
import java.util.List;

public class UrlHelperTests
{
    @Test
    public void when_asking_for_querystring_values_from_a_url_without_them___it_returns_an_empty_list()
    {
        List<AbstractMap.SimpleEntry<String, String>> results = UrlHelper.parseQuerystringValuesFrom("mypath.html?");
        assertEquals(0, results.size());
    }

    @Test
    public void when_asking_for_querystring_values_from_a_url___it_returns_a_list_of_1_value()
    {
        List<AbstractMap.SimpleEntry<String, String>> results = UrlHelper.parseQuerystringValuesFrom("mypath.html?val1=one");
        assertEquals(1, results.size());
        assertEquals("val1", results.get(0).getKey());
        assertEquals("one", results.get(0).getValue());
    }

    @Test
    public void when_asking_for_querystring_values_from_a_url___it_returns_a_list_of_2_values()
    {
        List<AbstractMap.SimpleEntry<String, String>> results = UrlHelper.parseQuerystringValuesFrom("mypath.html?val1=one&val2=two");
        assertEquals(2, results.size());
    }

    @Test
    public void when_asking_for_querystring_values_from_a_url___it_returns_the_correctly_mapped_querystring_key_and_value()
    {
        List<AbstractMap.SimpleEntry<String, String>> results = UrlHelper.parseQuerystringValuesFrom("mypath.html?val1=one");
        assertEquals("val1", results.get(0).getKey());
        assertEquals("one", results.get(0).getValue());
    }

    @Test
    public void when_asking_for_querystring_values_from_an_actual_url___it_returns_the_correct_values()
    {
        String url = "http://localhost:5000/some-script-url.shipwreck?variable_1=my%20value%20of%201&variable_2=yet%20another%20value%20of%20something";
        List<AbstractMap.SimpleEntry<String, String>> results = UrlHelper.parseQuerystringValuesFrom(url);

        assertEquals("variable_1", results.get(0).getKey());
        assertEquals("my value of 1", results.get(0).getValue());

        assertEquals("variable_2", results.get(1).getKey());
        assertEquals("yet another value of something", results.get(1).getValue());
    }
}

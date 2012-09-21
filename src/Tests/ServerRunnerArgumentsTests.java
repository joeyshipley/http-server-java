package Tests;

import org.junit.*;
import static junit.framework.Assert.*;
import Core.ServerRunnerArguments;

public class ServerRunnerArgumentsTests
{
    @Test
    public void when_building_the_server_runner_arguments_and_nothing_was_passed_in_make_sure_the_defaults_are_set()
    {
        String[] args = new String[4];
        ServerRunnerArguments results = ServerRunnerArguments.createFrom(args);
        assertEquals(results.Port, 5000);
        assertEquals(results.Directory, "public");
    }

    @Test
    public void when_building_the_server_runner_arguments_it_maps_the_port_correctly()
    {
        String[] args = new String[4];
        args[0] = "-p";
        args[1] = "2020";
        ServerRunnerArguments results = ServerRunnerArguments.createFrom(args);
        assertEquals(results.Port, 2020);
    }

    @Test
    public void when_building_the_server_runner_arguments_it_maps_the_directory_correctly()
    {
        String[] args = new String[4];
        args[1] = "-d";
        args[2] = "shipwreck";
        ServerRunnerArguments results = ServerRunnerArguments.createFrom(args);
        assertEquals(results.Directory, "shipwreck");
    }
}

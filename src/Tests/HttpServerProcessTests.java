package Tests;

import Tests.Helpers.*;
import org.junit.*;
import static junit.framework.Assert.*;
import java.io.*;
import java.net.*;
import Core.*;

public class HttpServerProcessTests
{
    private int TEST_PORT = 6000;
    private HttpServerProcess _SUT = null;
    private ServerSocket _serverSocket;

    @Before
    public void beforeEach()
         throws IOException
    {
        _serverSocket = new ServerSocket(TEST_PORT);
        _SUT = HttpServerProcess.createFrom(_serverSocket);
    }

    @After
    public void afterEach()
        throws IOException
    {
        _serverSocket.close();
        _SUT = null;
    }

    private void performCycleOnThread()
    {
        Thread thread = new Thread(new Runnable()
        {
            public void run()
            {
            try
            {
                _SUT.manageCommunicationCycle();
            } catch (Exception e) {}
            }
        });
    }

    @Test
    public void when_a_process_is_started_on_a_port___it_starts_listening_on_the_port_assigned_to_it()
        throws IOException
    {
        assertNotNull(_SUT);
        performCycleOnThread();

        Socket client = new Socket("localhost", TEST_PORT);
        assertFalse(client.isClosed());
        client.close();
    }

    @Test
    public void when_the_server_is_listening___and_something_talks_to_it_with_a_simple_request___it_returns_a_200_OK()
        throws IOException
    {
        performCycleOnThread();

        Socket client = new Socket("localhost", TEST_PORT);
        ClientCommunicationSimulator.sendRequest(client, "http://localhost:" + TEST_PORT + "/", "");
        client.close();
    }
}

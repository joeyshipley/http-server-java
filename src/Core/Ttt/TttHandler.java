package Core.Ttt;

import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

public class TttHandler
{
    public static String LOG_PATH = "lib/ttt/ttt_output_message.txt";
    private ConsoleGameFacade consoleGame;

    private TttHandler(ConsoleGameFacade consoleGame)
    {
        this.consoleGame = consoleGame;
    }

    public static TttHandler create()
    {
        ConsoleGameFacade consoleGame = ConsoleGame.create();
        return new TttHandler(consoleGame);
    }

    public static TttHandler create(ConsoleGameFacade consoleGame)
    {
        return new TttHandler(consoleGame);
    }

    public void startGame()
    {
        consoleGame.createGame();
    }

    public String performTurnCycle(String playerInput)
        throws Exception
    {
        sendInputToGame(playerInput);
        while(!receivedResponseFromConsoleGame())
        {
            // continue checking this until we've received a response from the console game.
        }
        return getOutput();
    }

    private boolean receivedResponseFromConsoleGame()
    {
        File logFile = new File(LOG_PATH);
        boolean fileExists = logFile.exists();
        return fileExists;
    }

    public void sendInputToGame(String input)
        throws InterruptedException
    {
        File logFile = new File(LOG_PATH);
        if(logFile.exists())
            logFile.delete();

        pauseForFileManipulation();

        consoleGame.passAlongInput(input);
    }

    public String getOutput()
        throws IOException
    {
        String contents = getLogFileContents();
        return contents;
    }

    public boolean isGameInProgress()
        throws IOException
    {
        String contents = getLogFileContents();
        return !contents.contains("Game Over!");
    }

    private void pauseForFileManipulation()
        throws InterruptedException
    {
        Thread.sleep(10L);
    }

    private String getLogFileContents()
        throws IOException
    {
        File log = new File(LOG_PATH);
        boolean exists = log.exists();
        URI uri = log.toURI();
        BufferedReader reader = new BufferedReader(new FileReader(uri.getPath()));
        String contents = "";
        String currentLine = "";
        while ((currentLine = reader.readLine()) != null) {
            contents += currentLine;
        }
        return contents;
    }
}

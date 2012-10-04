package Tests;

import Core.Ttt.ConsoleGameFacade;
import Core.Ttt.TttHandler;
import Tests.Fakes.FakeConsoleGame;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TttHandlerTests
{
    @Test
    public void when_starting_a_new_ttt_game___it_is_in_progress()
        throws Exception
    {
        TttHandler tttHandler = TttHandler.create();
        tttHandler.startGame();
        Thread.sleep(1L); // give it a chance to run the ruby code.

        boolean isGameInProgress = tttHandler.isGameInProgress();
        assertTrue(isGameInProgress);
    }

    @Test
    public void before_the_adapter_sends_information_to_the_game___it_deletes_the_log_file()
        throws Exception
    {
        File logFile = new File(TttHandler.LOG_PATH);
        if(!logFile.exists())
            logFile.createNewFile();
        logFile = null;

        ConsoleGameFacade fakeConsoleGame = new FakeConsoleGame();
        TttHandler tttHandler = TttHandler.create(fakeConsoleGame);
        tttHandler.sendInputToGame("1");

        logFile = new File(TttHandler.LOG_PATH);
        assertFalse(logFile.exists());
    }

    @Test
    public void when_the_adapter_receives_user_input___it_passes_it_to_the_console_game()
        throws InterruptedException
    {
        // safety first
        FakeConsoleGame.passAlongInputWasCalled = false;

        ConsoleGameFacade fakeConsoleGame = new FakeConsoleGame();
        TttHandler tttHandler = TttHandler.create(fakeConsoleGame);
        tttHandler.sendInputToGame("1");

        assertTrue(FakeConsoleGame.passAlongInputWasCalled);

        // cleanup
        FakeConsoleGame.passAlongInputWasCalled = false;
    }

    @Test
    public void when_the_adapter_asks_for_the_output___it_gets_it_from_the_log_files()
        throws Exception
    {
        TttHandler tttHandler = TttHandler.create();
        tttHandler.startGame();
        Thread.sleep(1L); // give it a chance to run the ruby code.
        String output = tttHandler.getOutput();

        boolean hasLogContents = output.contains("To continue in English");
        assertTrue(hasLogContents);
    }

    @Test
    public void when_performing_a_game_turn_cycle___it_passes_along_the_input_and_sends_back_the_response()
        throws Exception
    {
        File logFile = new File(TttHandler.LOG_PATH);
        if(logFile.exists())
            logFile.delete();

        TttHandler tttHandler = TttHandler.create();
        tttHandler.startGame();
        Thread.sleep(1L); // give it a chance to run the ruby code.

        String playerInput = "1";
        String response = tttHandler.performTurnCycle(playerInput);
        assertEquals("Fail!", "Choose your game board: [Any Key] 2D game or [3] 3D game:", response);
    }
}

package Tests;

import org.junit.*;
import static junit.framework.Assert.*;

import Core.Ttt.*;

import javax.script.ScriptException;
import java.io.IOException;

public class TttHandlerTests
{
    @Test
    public void when_starting_a_new_ttt_game___it_is_in_progress()
            throws Exception
    {
        TttHandler tttHandler = TttHandler.create();
        tttHandler.startGame();

        boolean isGameInProgress = tttHandler.isGameInProgress();
        assertTrue(isGameInProgress);
    }
}

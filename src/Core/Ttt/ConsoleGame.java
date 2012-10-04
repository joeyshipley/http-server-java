package Core.Ttt;

import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

public class ConsoleGame implements ConsoleGameFacade
{
    private ScriptingContainer container;

    private ConsoleGame()
    {
        container = new ScriptingContainer(LocalVariableBehavior.PERSISTENT);
    }

    public static ConsoleGame create()
    {
        return new ConsoleGame();
    }

    public void createGame()
    {
        container.runScriptlet(PathType.RELATIVE, "lib/ttt/ttt_game_runner_adapter.rb");
        container.runScriptlet("adapter = TttGameRunnerAdapter.new");
        container.runScriptlet("adapter.start_game");
    }

    public void passAlongInput(String input)
    {
        container.runScriptlet("adapter.input " + input);
    }
}

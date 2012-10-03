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
    private ScriptingContainer container;

    private TttHandler()
    {
        container = new ScriptingContainer(LocalVariableBehavior.PERSISTENT);
    }

    public static TttHandler create()
    {
        return new TttHandler();
    }

    public void startGame() throws ScriptException {
        container.runScriptlet(PathType.RELATIVE, "lib/ttt/ttt_game_runner_adapter.rb");
        container.runScriptlet("adapter = TttGameRunnerAdapter.new");
        container.runScriptlet("adapter.start_game");
    }

    public boolean isGameInProgress()
        throws IOException
    {
        File log = new File("lib/ttt/ttt_output_message.txt");
        boolean exists = log.exists();
        URI uri = log.toURI();
        BufferedReader reader = new BufferedReader(new FileReader(uri.getPath()));
        String contents = "";
        String currentLine = "";
        while ((currentLine = reader.readLine()) != null) {
            contents += currentLine;
        }
        return !contents.contains("Game Over!");
    }
}

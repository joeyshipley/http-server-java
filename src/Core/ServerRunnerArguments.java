package Core;

public class ServerRunnerArguments
{
    public int Port;
    public String Directory;

    private ServerRunnerArguments(int port, String directory)
    {
        Port = port;
        Directory = directory;
    }

    public static ServerRunnerArguments createFrom(String[] args)
    {
        int port = Constants.PORT;
        String directory = Constants.DIRECTORY;

        // TODO: fragile, could definitely use some love & validation

        for(int i = 0; i < args.length; i++)
        {
            if(args[i] == null) { continue; }

            if(args[i].equals("-p"))
            {
                port = Integer.parseInt(args[i + 1]);
            }

            if(args[i].equals("-d"))
            {
                directory = args[i + 1];
            }
        }

        return new ServerRunnerArguments(port, directory);
    }
}
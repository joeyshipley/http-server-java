package Tests.Fakes;

import Core.Ttt.ConsoleGameFacade;

public class FakeConsoleGame implements ConsoleGameFacade
{
    public static boolean passAlongInputWasCalled = false;

    public void createGame() {}
    public void passAlongInput(String input)
    {
        passAlongInputWasCalled = true;
    }
}
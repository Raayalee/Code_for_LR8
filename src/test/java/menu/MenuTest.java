package menu;

import command.Command;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    private final PrintStream originalOut = System.out;
    private final java.io.InputStream originalIn = System.in;

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void shouldRunMenuAndExitImmediately() {
        System.setIn(new ByteArrayInputStream("exit\n".getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Menu menu = new Menu("Test Menu");
        menu.run();

        assertTrue(out.toString().contains("Thanks for using the program"));
    }

    @Test
    void shouldShowHelpCommand() {
        System.setIn(new ByteArrayInputStream("help\nexit\n".getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Menu menu = new Menu("Test Menu");
        menu.run();

        assertTrue(out.toString().contains("Available commands"));
    }

    @Test
    void shouldExecuteRegisteredCommandWithoutParameters() {
        System.setIn(new ByteArrayInputStream("1\nexit\n".getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Menu menu = new Menu("Test Menu");

        menu.addCommand("1", new Command() {
            @Override
            public void execute(String parameters) {
                System.out.println("COMMAND_EXECUTED");
            }

            @Override
            public String getDescription() {
                return "Test command";
            }
        });

        menu.run();

        assertTrue(out.toString().contains("COMMAND_EXECUTED"));
    }

    @Test
    void shouldExecuteRegisteredCommandWithParameters() {
        System.setIn(new ByteArrayInputStream("1 param1 param2\nexit\n".getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Menu menu = new Menu("Test Menu");

        menu.addCommand("1", new Command() {
            @Override
            public void execute(String parameters) {
                System.out.println("PARAMS=" + parameters);
            }

            @Override
            public String getDescription() {
                return "Test command";
            }
        });

        menu.run();

        assertTrue(out.toString().contains("PARAMS=param1 param2"));
    }

    @Test
    void shouldHandleUnknownCommand() {
        System.setIn(new ByteArrayInputStream("unknown\nexit\n".getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Menu menu = new Menu("Test Menu");
        menu.run();

        assertTrue(out.toString().contains("Unknown command"));
    }

    @Test
    void shouldHandleExceptionInsideCommand() {
        System.setIn(new ByteArrayInputStream("1\nexit\n".getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Menu menu = new Menu("Test Menu");

        menu.addCommand("1", new Command() {
            @Override
            public void execute(String parameters) {
                throw new RuntimeException("Boom");
            }

            @Override
            public String getDescription() {
                return "Exploding command";
            }
        });

        menu.run();

        assertTrue(out.toString().contains("Error while executing command"));
    }

    @Test
    void shouldRunMenuViaExecuteMethod() {
        System.setIn(new ByteArrayInputStream("exit\n".getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Menu menu = new Menu("Test Menu");
        menu.execute("");

        assertTrue(out.toString().contains("Welcome to the tariff management system"));
    }
}

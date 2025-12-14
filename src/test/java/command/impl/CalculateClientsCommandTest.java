package command.impl;

import command.CalculateClientsCommand;
import model.BasicTariff;
import model.Client;
import model.Tariff;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculateClientsCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void shouldHandleNullTariffs() {
        CalculateClientsCommand cmd =
                new CalculateClientsCommand(null, new ArrayList<>());

        cmd.execute("");

        String output = outContent.toString();
        assertTrue(output.contains("No tariffs found"));
    }

    @Test
    void shouldHandleEmptyTariffs() {
        CalculateClientsCommand cmd =
                new CalculateClientsCommand(new ArrayList<>(), new ArrayList<>());

        cmd.execute("");

        String output = outContent.toString();
        assertTrue(output.contains("No tariffs found"));
    }

    @Test
    void shouldHandleNullClients() {
        List<Tariff> tariffs = new ArrayList<>();
        tariffs.add(new BasicTariff("Basic", 100, 100, 10));

        CalculateClientsCommand cmd =
                new CalculateClientsCommand(tariffs, null);

        cmd.execute("");

        String output = outContent.toString();
        assertTrue(output.contains("No clients found"));
        assertTrue(output.contains("Basic"));
        assertTrue(output.contains(": 0"));
    }

    @Test
    void shouldHandleNoClients() {
        List<Tariff> tariffs = new ArrayList<>();
        tariffs.add(new BasicTariff("Basic", 100, 100, 10));

        CalculateClientsCommand cmd =
                new CalculateClientsCommand(tariffs, new ArrayList<>());

        cmd.execute("");

        String output = outContent.toString();
        assertTrue(output.contains("No clients found"));
        assertTrue(output.contains("Basic"));
        assertTrue(output.contains(": 0"));
    }

    @Test
    void shouldCalculateClientsNormally() {
        List<Tariff> tariffs = new ArrayList<>();
        BasicTariff tariff = new BasicTariff("Basic", 100, 100, 10);
        tariffs.add(tariff);

        Client c1 = new Client("A", "1");
        Client c2 = new Client("B", "2");

        c1.subscribeToTariff(tariff);
        c2.subscribeToTariff(tariff);

        List<Client> clients = List.of(c1, c2);

        CalculateClientsCommand cmd =
                new CalculateClientsCommand(tariffs, clients);

        cmd.execute("");

        String output = outContent.toString();
        assertTrue(output.contains("Total clients: 2"));
        assertTrue(output.contains("Basic"));
        assertTrue(output.contains(": 2"));
    }
}

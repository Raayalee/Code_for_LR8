package command.impl;

import command.DeleteTariffCommand;
import factory.TariffFactory;
import model.Client;
import model.Tariff;
import model.TariffType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DeleteTariffCommandTest {

    private List<Tariff> tariffs;
    private List<Client> clients;
    private TariffFactory factory;

    private Tariff basic;
    private Tariff premium;

    @BeforeEach
    void setUp() {
        tariffs = new ArrayList<>();
        clients = new ArrayList<>();
        factory = new TariffFactory();

        basic = factory.createTariff(
                TariffType.BASIC,
                "Basic",
                100,
                Map.of("freeMinutes", 50, "internetGB", 5.0)
        );

        premium = factory.createTariff(
                TariffType.PREMIUM,
                "Premium",
                300,
                Map.of(
                        "internationalCalls", true,
                        "roaming", true,
                        "entertainment", true
                )
        );

        tariffs.add(basic);
        tariffs.add(premium);

        Client c1 = new Client("Alice", "111");
        Client c2 = new Client("Bob", "222");

        c1.subscribeToTariff(premium);
        c2.subscribeToTariff(premium);

        clients.add(c1);
        clients.add(c2);
    }

    // happy-path (y)
    @Test
    void shouldDeleteTariff_whenConfirmedYes() {
        DeleteTariffCommand command =
                new DeleteTariffCommand(tariffs, clients, new Scanner("y\n"));

        command.execute("Premium");

        assertEquals(1, tariffs.size());
        assertEquals("Basic", tariffs.get(0).getName());

        for (Client c : clients) {
            assertEquals(basic, c.getCurrentTariff());
        }
    }

    // cancel (n)
    @Test
    void shouldNotDeleteTariff_whenConfirmedNo() {
        DeleteTariffCommand command =
                new DeleteTariffCommand(tariffs, clients, new Scanner("n\n"));

        command.execute("Premium");

        assertEquals(2, tariffs.size());
    }

    // invalid → y (while-loop)
    @Test
    void shouldAskAgainOnInvalidInput() {
        DeleteTariffCommand command =
                new DeleteTariffCommand(tariffs, clients, new Scanner("abc\ny\n"));

        command.execute("Premium");

        assertEquals(1, tariffs.size());
        assertEquals("Basic", tariffs.get(0).getName());
    }

    // tariff not found
    @Test
    void shouldDoNothing_whenTariffNotFound() {
        DeleteTariffCommand command =
                new DeleteTariffCommand(tariffs, clients, new Scanner("y\n"));

        command.execute("Unknown");

        assertEquals(2, tariffs.size());
    }

    // empty parameters
    @Test
    void shouldDoNothing_whenParametersEmpty() {
        DeleteTariffCommand command =
                new DeleteTariffCommand(tariffs, clients, new Scanner("y\n"));

        command.execute("  ");

        assertEquals(2, tariffs.size());
    }

    // parameters = null
    @Test
    void shouldDoNothing_whenParametersNull() {
        DeleteTariffCommand command =
                new DeleteTariffCommand(tariffs, clients, new Scanner("y\n"));

        command.execute(null);

        assertEquals(2, tariffs.size());
    }

    // no BASIC tariff available
    @Test
    void shouldNotDelete_whenNoBasicTariffExists() {

        tariffs.removeIf(t -> t.getType() == TariffType.BASIC);

        DeleteTariffCommand command =
                new DeleteTariffCommand(tariffs, clients, new Scanner("y\n"));

        command.execute("Premium");

        assertEquals(1, tariffs.size());
        assertEquals("Premium", tariffs.get(0).getName());
    }

    // tariff exists but has NO subscribers
    @Test
    void shouldDeleteTariff_whenNoSubscribers() {

        clients.clear();

        DeleteTariffCommand command =
                new DeleteTariffCommand(tariffs, clients, new Scanner("y\n"));

        command.execute("Premium");

        assertEquals(1, tariffs.size());
        assertEquals("Basic", tariffs.get(0).getName());
    }

    // confirm using full words "yes" / "no"
    @Test
    void shouldHandleYesAndNoWords() {

        // yes
        DeleteTariffCommand yesCommand =
                new DeleteTariffCommand(tariffs, clients, new Scanner("yes\n"));

        yesCommand.execute("Premium");

        assertEquals(1, tariffs.size());
        assertEquals("Basic", tariffs.get(0).getName());

        // відновлюємо стан
        setUp();

        // no
        DeleteTariffCommand noCommand =
                new DeleteTariffCommand(tariffs, clients, new Scanner("no\n"));

        noCommand.execute("Premium");

        assertEquals(2, tariffs.size());
    }
}

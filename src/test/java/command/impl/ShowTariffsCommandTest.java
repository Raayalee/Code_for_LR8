package command.impl;

import command.ShowTariffsCommand;
import model.Tariff;
import model.TariffType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShowTariffsCommandTest {

    // STUB Tariff (для тестів)
    static class TariffStub extends Tariff {

        public TariffStub(String name, double monthlyFee) {
            super(name, monthlyFee, TariffType.BASIC);
        }

        @Override
        public String calculateAdditionalFeatures() {
            return "stub";
        }
    }

    @Test
    void descriptionCorrect() {
        ShowTariffsCommand command =
                new ShowTariffsCommand(new ArrayList<>());

        assertEquals(
                "Show all tariffs",
                command.getDescription()
        );
    }

    @Test
    void noTariffsPrintsMessage() {
        ShowTariffsCommand command =
                new ShowTariffsCommand(new ArrayList<>());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        command.execute(null);

        String printed = out.toString().trim();

        assertEquals(
                "No tariffs available.",
                printed
        );
    }

    @Test
    void tariffsPrintedCorrectly() {
        List<Tariff> tariffs = List.of(
                new TariffStub("Basic", 100),
                new TariffStub("Premium", 300)
        );

        ShowTariffsCommand command =
                new ShowTariffsCommand(tariffs);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        command.execute(null);

        String printed = out.toString();

        assertTrue(printed.contains("Tariffs:"));
        assertTrue(printed.contains("Basic"));
        assertTrue(printed.contains("Premium"));
    }
}

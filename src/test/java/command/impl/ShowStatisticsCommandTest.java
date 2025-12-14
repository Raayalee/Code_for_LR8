package command.impl;

import command.ShowStatisticsCommand;
import model.Tariff;
import model.TariffType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShowStatisticsCommandTest {

    // Tariff STUB
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
    void descriptionIsCorrect() {
        ShowStatisticsCommand command =
                new ShowStatisticsCommand(new ArrayList<>());

        assertEquals(
                "Show statistics (min, max, avg price, count)",
                command.getDescription()
        );
    }

    @Test
    void executeWithEmptyTariffListPrintsOnlyCount() {
        List<Tariff> tariffs = new ArrayList<>();
        ShowStatisticsCommand command =
                new ShowStatisticsCommand(tariffs);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        command.execute(null);

        String output = out.toString();

        assertTrue(output.contains("Tariff count: 0"));
        assertFalse(output.contains("Min price"));
        assertFalse(output.contains("Max price"));
        assertFalse(output.contains("Average price"));
    }

    @Test
    void executeWithTariffsPrintsCorrectStatistics() {
        List<Tariff> tariffs = List.of(
                new TariffStub("Cheap", 50.0),
                new TariffStub("Medium", 100.0),
                new TariffStub("Expensive", 300.0)
        );

        ShowStatisticsCommand command =
                new ShowStatisticsCommand(tariffs);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        command.execute(null);

        String output = out.toString();

        assertTrue(output.contains("Tariff count: 3"));
        assertTrue(output.contains("Min price: 50.00 (Cheap)"));
        assertTrue(output.contains("Max price: 300.00 (Expensive)"));
        assertTrue(output.contains("Average price: 150.00"));
    }

    @Test
    void executeWithSingleTariffStillWorksCorrectly() {
        List<Tariff> tariffs = List.of(
                new TariffStub("OnlyOne", 120.0)
        );

        ShowStatisticsCommand command =
                new ShowStatisticsCommand(tariffs);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        command.execute(null);

        String output = out.toString();

        assertTrue(output.contains("Tariff count: 1"));
        assertTrue(output.contains("Min price: 120.00 (OnlyOne)"));
        assertTrue(output.contains("Max price: 120.00 (OnlyOne)"));
        assertTrue(output.contains("Average price: 120.00"));
    }
}

package command.impl;

import command.SortTariffsCommand;
import model.Tariff;
import model.TariffType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortTariffsCommandTest {

    // STUB Tariff
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
        SortTariffsCommand command =
                new SortTariffsCommand(new ArrayList<>());

        assertEquals(
                "Sort tariffs by price. Usage: asc|desc",
                command.getDescription()
        );
    }

    @Test
    void sortAscendingExplicit() {
        List<Tariff> tariffs = new ArrayList<>(List.of(
                new TariffStub("Expensive", 300),
                new TariffStub("Cheap", 50),
                new TariffStub("Medium", 100)
        ));

        SortTariffsCommand command =
                new SortTariffsCommand(tariffs);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        command.execute("asc");

        assertEquals(50, tariffs.get(0).getMonthlyFee());
        assertEquals(100, tariffs.get(1).getMonthlyFee());
        assertEquals(300, tariffs.get(2).getMonthlyFee());

        assertTrue(out.toString().contains("ascending"));
    }

    @Test
    void sortDescendingExplicit() {
        List<Tariff> tariffs = new ArrayList<>(List.of(
                new TariffStub("Cheap", 50),
                new TariffStub("Medium", 100),
                new TariffStub("Expensive", 300)
        ));

        SortTariffsCommand command =
                new SortTariffsCommand(tariffs);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        command.execute("desc");

        assertEquals(300, tariffs.get(0).getMonthlyFee());
        assertEquals(100, tariffs.get(1).getMonthlyFee());
        assertEquals(50, tariffs.get(2).getMonthlyFee());

        assertTrue(out.toString().contains("descending"));
    }

    @Test
    void defaultIsAscendingWhenNull() {
        List<Tariff> tariffs = new ArrayList<>(List.of(
                new TariffStub("Medium", 100),
                new TariffStub("Cheap", 50)
        ));

        SortTariffsCommand command =
                new SortTariffsCommand(tariffs);

        command.execute(null);

        assertEquals(50, tariffs.get(0).getMonthlyFee());
        assertEquals(100, tariffs.get(1).getMonthlyFee());
    }

    @Test
    void unknownParameterTreatedAsAscending() {
        List<Tariff> tariffs = new ArrayList<>(List.of(
                new TariffStub("Medium", 100),
                new TariffStub("Cheap", 50)
        ));

        SortTariffsCommand command =
                new SortTariffsCommand(tariffs);

        command.execute("whatever");

        assertEquals(50, tariffs.get(0).getMonthlyFee());
        assertEquals(100, tariffs.get(1).getMonthlyFee());
    }
}

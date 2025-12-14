package command.impl;

import command.FindTariffsCommand;
import model.Tariff;
import model.TariffType;
import org.junit.jupiter.api.Test;
import service.DataValidator;
import service.SearchService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FindTariffsCommandTest {

    // STUB для Tariff
    static class TariffStub extends Tariff {

        public TariffStub(String name, double monthlyFee) {
            super(name, monthlyFee, TariffType.BASIC);
            calculateAdditionalFeatures();
        }

        @Override
        public String calculateAdditionalFeatures() {
            return "...";
        }

        @Override
        public String toString() {
            return getName() + " (" + getMonthlyFee() + ")";
        }
    }

    @Test
    void parametersNull() {
        FindTariffsCommand command = new FindTariffsCommand(
                new ArrayList<>(),
                new SearchService(),
                new DataValidator()
        );

        String result = command.process(null);

        assertEquals(
                "Provide min and max price. Example: 100 500\n",
                result
        );
    }

    @Test
    void parametersEmpty() {
        FindTariffsCommand command = new FindTariffsCommand(
                new ArrayList<>(),
                new SearchService(),
                new DataValidator()
        );

        String result = command.process("   ");

        assertEquals(
                "Provide min and max price. Example: 100 500\n",
                result
        );
    }

    @Test
    void oneNumberOnly() {
        FindTariffsCommand command = new FindTariffsCommand(
                new ArrayList<>(),
                new SearchService(),
                new DataValidator()
        );

        String result = command.process("100");

        assertEquals(
                "Need two numbers: min max\n",
                result
        );
    }

    @Test
    void invalidRange() {
        DataValidator validator = new DataValidator() {
            @Override
            public boolean validatePriceRange(double min, double max) {
                return false;
            }
        };

        FindTariffsCommand command = new FindTariffsCommand(
                new ArrayList<>(),
                new SearchService(),
                validator
        );

        String result = command.process("500 100");

        assertEquals("Invalid price range.\n", result);
    }

    @Test
    void noTariffsFound() {
        SearchService searchService = new SearchService() {
            @Override
            public List<Tariff> filterByPriceRange(
                    List<Tariff> tariffs, double min, double max) {
                return List.of();
            }
        };

        FindTariffsCommand command = new FindTariffsCommand(
                new ArrayList<>(),
                searchService,
                new DataValidator()
        );

        String result = command.process("100 500");

        assertEquals("No tariffs in the given range.\n", result);
    }

    @Test
    void tariffsFound() {
        SearchService searchService = new SearchService() {
            @Override
            public List<Tariff> filterByPriceRange(
                    List<Tariff> tariffs, double min, double max) {
                return List.of(
                        new TariffStub("Basic", 100),
                        new TariffStub("Premium", 300)
                );
            }
        };

        FindTariffsCommand command = new FindTariffsCommand(
                new ArrayList<>(),
                searchService,
                new DataValidator()
        );

        String result = command.process("50 400");

        assertTrue(result.contains("Found tariffs"));
        assertTrue(result.contains("Basic"));
        assertTrue(result.contains("Premium"));
    }

    @Test
    void invalidNumberFormat() {
        FindTariffsCommand command = new FindTariffsCommand(
                new ArrayList<>(),
                new SearchService(),
                new DataValidator()
        );

        String result = command.process("abc def");

        assertTrue(result.startsWith("Error while searching:"));
    }

    @Test
    void descriptionIsCorrect() {
        FindTariffsCommand command = new FindTariffsCommand(
                new ArrayList<>(),
                new SearchService(),
                new DataValidator()
        );

        assertEquals(
                "Find tariffs in price range. Usage: min max",
                command.getDescription()
        );
    }

    @Test
    void executePrintsResult() {
        SearchService searchService = new SearchService() {
            @Override
            public List<Tariff> filterByPriceRange(
                    List<Tariff> tariffs, double min, double max) {
                return List.of(new TariffStub("Basic", 100));
            }
        };

        FindTariffsCommand command = new FindTariffsCommand(
                new ArrayList<>(),
                searchService,
                new DataValidator()
        );

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        command.execute("50 200");

        String printed = out.toString();

        assertTrue(printed.contains("Found tariffs"));
        assertTrue(printed.contains("Basic"));
    }
}

package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicTariffTest {

    @Test
    void constructorAndGettersWorkCorrectly() {
        BasicTariff tariff = new BasicTariff(
                "Basic Plan",
                99.99,
                300,
                10.5
        );

        assertEquals("Basic Plan", tariff.getName());
        assertEquals(99.99, tariff.getMonthlyFee());
        assertEquals(300, tariff.getFreeMinutes());
        assertEquals(10.5, tariff.getInternetGB());
        assertEquals(TariffType.BASIC, tariff.getType());
        assertEquals(0, tariff.getClientCount());
    }

    @Test
    void calculateAdditionalFeaturesReturnsCorrectString() {
        BasicTariff tariff = new BasicTariff(
                "Basic",
                100.0,
                200,
                15.0
        );

        String features = tariff.calculateAdditionalFeatures();

        assertEquals(
                "freeMinutes=200, internetGB=15.0",
                features
        );
    }

    @Test
    void toStringContainsAllImportantData() {
        BasicTariff tariff = new BasicTariff(
                "Starter",
                50.0,
                100,
                5.5
        );

        String text = tariff.toString();

        assertTrue(text.contains("[BASIC]"));
        assertTrue(text.contains("name='Starter'"));
        assertTrue(text.contains("fee=50.00"));
        assertTrue(text.contains("freeMinutes=100"));
        assertTrue(text.contains("internetGB=5.5"));
    }

    @Test
    void clientCountOperationsWork() {
        BasicTariff tariff = new BasicTariff(
                "Basic",
                80.0,
                150,
                8.0
        );

        assertEquals(0, tariff.getClientCount());

        tariff.incrementClientCount();
        tariff.incrementClientCount();
        assertEquals(2, tariff.getClientCount());

        tariff.decrementClientCount();
        assertEquals(1, tariff.getClientCount());

        tariff.decrementClientCount();
        tariff.decrementClientCount(); // нижче 0 не піде
        assertEquals(0, tariff.getClientCount());
    }
}

package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PremiumTariffTest {

    @Test
    void constructorAndGettersWorkCorrectly() {
        PremiumTariff tariff = new PremiumTariff(
                "Premium Plus",
                199.99,
                true,
                false,
                true
        );

        assertEquals("Premium Plus", tariff.getName());
        assertEquals(199.99, tariff.getMonthlyFee());
        assertEquals(TariffType.PREMIUM, tariff.getType());

        assertTrue(tariff.hasInternationalCalls());
        assertFalse(tariff.hasRoaming());
        assertTrue(tariff.hasEntertainmentPackage());

        assertEquals(0, tariff.getClientCount());
    }

    @Test
    void calculateAdditionalFeaturesReturnsCorrectString() {
        PremiumTariff tariff = new PremiumTariff(
                "Premium",
                150.0,
                false,
                true,
                false
        );

        String features = tariff.calculateAdditionalFeatures();

        assertEquals(
                "internationalCalls=false, roaming=true, entertainment=false",
                features
        );
    }

    @Test
    void toStringContainsAllImportantData() {
        PremiumTariff tariff = new PremiumTariff(
                "Ultra",
                250.0,
                true,
                true,
                true
        );

        String text = tariff.toString();

        assertTrue(text.contains("[PREMIUM]"));
        assertTrue(text.contains("name='Ultra'"));
        assertTrue(text.contains("fee=250.00"));
        assertTrue(text.contains("internationalCalls=true"));
        assertTrue(text.contains("roaming=true"));
        assertTrue(text.contains("entertainment=true"));
    }

    @Test
    void clientCountOperationsInheritedFromTariffWork() {
        PremiumTariff tariff = new PremiumTariff(
                "Premium",
                180.0,
                true,
                false,
                false
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

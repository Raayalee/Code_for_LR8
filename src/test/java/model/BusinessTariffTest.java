package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessTariffTest {

    @Test
    void constructorAndBasicFieldsWorkCorrectly() {
        BusinessTariff tariff = new BusinessTariff(
                "Business Pro",
                299.99,
                true,
                true,
                false
        );

        assertEquals("Business Pro", tariff.getName());
        assertEquals(299.99, tariff.getMonthlyFee());
        assertEquals(TariffType.BUSINESS, tariff.getType());
        assertEquals(0, tariff.getClientCount());
    }

    @Test
    void calculateAdditionalFeaturesReturnsCorrectString() {
        BusinessTariff tariff = new BusinessTariff(
                "Biz",
                200.0,
                true,
                false,
                true
        );

        String features = tariff.calculateAdditionalFeatures();

        assertEquals(
                "prioritySupport=true, accountManager=false, roaming=true",
                features
        );
    }

    @Test
    void toStringContainsAllImportantData() {
        BusinessTariff tariff = new BusinessTariff(
                "Enterprise",
                500.0,
                true,
                true,
                true
        );

        String text = tariff.toString();

        assertTrue(text.contains("[BUSINESS]"));
        assertTrue(text.contains("name='Enterprise'"));
        assertTrue(text.contains("fee=500.00"));
        assertTrue(text.contains("prioritySupport=true"));
        assertTrue(text.contains("accountManager=true"));
        assertTrue(text.contains("roaming=true"));
    }

    @Test
    void clientCountOperationsInheritedFromTariffWork() {
        BusinessTariff tariff = new BusinessTariff(
                "Biz",
                150.0,
                false,
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
        tariff.decrementClientCount(); // не нижче 0
        assertEquals(0, tariff.getClientCount());
    }
}

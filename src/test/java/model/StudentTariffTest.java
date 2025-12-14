package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTariffTest {

    @Test
    void constructorAndGettersWorkCorrectly() {
        StudentTariff tariff = new StudentTariff(
                "Student Plan",
                120.0,
                25.0,
                true
        );

        assertEquals("Student Plan", tariff.getName());
        assertEquals(120.0, tariff.getMonthlyFee());
        assertEquals(TariffType.STUDENT, tariff.getType());

        assertEquals(25.0, tariff.getDiscountPercent());
        assertTrue(tariff.isRequiresVerification());

        assertEquals(0, tariff.getClientCount());
    }

    @Test
    void discountedMonthlyFeeCalculatedCorrectly() {
        StudentTariff tariff = new StudentTariff(
                "Student",
                200.0,
                50.0,
                false
        );

        // 200 - 50% = 100
        double discounted = tariff.getDiscountedMonthlyFee();

        assertEquals(100.0, discounted);
    }

    @Test
    void calculateAdditionalFeaturesReturnsCorrectString() {
        StudentTariff tariff = new StudentTariff(
                "Student",
                100.0,
                15.5,
                true
        );

        String features = tariff.calculateAdditionalFeatures();

        assertEquals(
                "discountPercent=15.5, studentIdVerification=true",
                features
        );
    }

    @Test
    void toStringContainsAllImportantData() {
        StudentTariff tariff = new StudentTariff(
                "Uni",
                80.0,
                20.0,
                false
        );

        String text = tariff.toString();

        assertTrue(text.contains("[STUDENT]"));
        assertTrue(text.contains("name='Uni'"));
        assertTrue(text.contains("fee=80.0"));
        assertTrue(text.contains("discountPercent=20.0"));
        assertTrue(text.contains("studentIdVerification=false"));
    }

    // Валідація конструктора
    @Test
    void constructorThrowsExceptionWhenDiscountLessThanZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new StudentTariff("Bad", 100.0, -5.0, true)
        );
    }

    @Test
    void constructorThrowsExceptionWhenDiscountGreaterThanHundred() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new StudentTariff("Bad", 100.0, 150.0, false)
        );
    }

    @Test
    void clientCountOperationsInheritedFromTariffWork() {
        StudentTariff tariff = new StudentTariff(
                "Student",
                90.0,
                10.0,
                true
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

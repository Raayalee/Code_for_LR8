package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FamilyTariffTest {

    @Test
    void constructorAndGettersWorkCorrectly() {
        FamilyTariff tariff = new FamilyTariff(
                "Family Plan",
                200.0,
                4,
                40.0,
                20.0
        );

        assertEquals("Family Plan", tariff.getName());
        assertEquals(200.0, tariff.getMonthlyFee());
        assertEquals(TariffType.FAMILY, tariff.getType());
        assertEquals(4, tariff.getNumberOfFamilyMembers());
        assertEquals(40.0, tariff.getSharedInternetGB());
        assertEquals(20.0, tariff.getFamilyDiscountPercent());
        assertEquals(0, tariff.getClientCount());
    }

    @Test
    void effectiveMonthlyFeePerMemberCalculatedCorrectly() {
        FamilyTariff tariff = new FamilyTariff(
                "Family",
                300.0,
                3,
                30.0,
                10.0
        );

        // 300 - 10% = 270 / 3 = 90
        double effective = tariff.getEffectiveMonthlyFeePerMember();

        assertEquals(90.0, effective);
    }

    @Test
    void calculateAdditionalFeaturesReturnsCorrectString() {
        FamilyTariff tariff = new FamilyTariff(
                "Family",
                150.0,
                5,
                50.0,
                15.5
        );

        String features = tariff.calculateAdditionalFeatures();

        assertEquals(
                "members=5, sharedGB=50.0, familyDiscountPercent=15.5",
                features
        );
    }

    @Test
    void toStringContainsAllImportantData() {
        FamilyTariff tariff = new FamilyTariff(
                "Home",
                120.0,
                2,
                20.0,
                5.0
        );

        String text = tariff.toString();

        assertTrue(text.contains("[FAMILY]"));
        assertTrue(text.contains("name='Home'"));
        assertTrue(text.contains("fee=120.00"));
        assertTrue(text.contains("members=2"));
        assertTrue(text.contains("sharedGB=20.0"));
        assertTrue(text.contains("familyDiscountPercent=5.0"));
    }

    // Валідація конструктора
    @Test
    void constructorThrowsExceptionWhenMembersLessThanOne() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FamilyTariff("Bad", 100.0, 0, 10.0, 10.0)
        );
    }

    @Test
    void constructorThrowsExceptionWhenSharedInternetNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FamilyTariff("Bad", 100.0, 2, -1.0, 10.0)
        );
    }

    @Test
    void constructorThrowsExceptionWhenDiscountOutOfRange() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FamilyTariff("Bad", 100.0, 2, 10.0, 120.0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new FamilyTariff("Bad", 100.0, 2, 10.0, -5.0)
        );
    }

    @Test
    void clientCountOperationsInheritedFromTariffWork() {
        FamilyTariff tariff = new FamilyTariff(
                "Family",
                180.0,
                3,
                25.0,
                10.0
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

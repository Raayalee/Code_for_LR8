package factory;

import model.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TariffFactoryTest {

    private final TariffFactory factory = new TariffFactory();

    @Test
    void createBasicWithAllParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("freeMinutes", 100);
        params.put("internetGB", 10.0);

        Tariff tariff = factory.createTariff(
                TariffType.BASIC,
                "Basic",
                100,
                params
        );
        assertTrue(tariff instanceof BasicTariff);
    }

    @Test
    void createBasicWithMissingParamsUsesDefaults() {
        Tariff tariff = factory.createTariff(
                TariffType.BASIC,
                "Basic",
                100,
                new HashMap<>()
        );
        assertTrue(tariff instanceof BasicTariff);
    }

    @Test
    void createBasicWithWrongParamTypes() {
        Map<String, Object> params = new HashMap<>();
        params.put("freeMinutes", "x");
        params.put("internetGB", "y");

        Tariff tariff = factory.createTariff(
                TariffType.BASIC,
                "Basic",
                100,
                params
        );
        assertTrue(tariff instanceof BasicTariff);
    }

    @Test
    void createBasicWithNullParams() {
        Tariff tariff = factory.createTariff(
                TariffType.BASIC,
                "Basic",
                100,
                null
        );
        assertTrue(tariff instanceof BasicTariff);
    }

    @Test
    void createPremiumAllParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("internationalCalls", true);
        params.put("roaming", true);
        params.put("entertainment", true);

        Tariff tariff = factory.createTariff(
                TariffType.PREMIUM,
                "Premium",
                200,
                params
        );
        assertTrue(tariff instanceof PremiumTariff);
    }

    @Test
    void createPremiumMissingParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("roaming", true);

        Tariff tariff = factory.createTariff(
                TariffType.PREMIUM,
                "Premium",
                200,
                params
        );
        assertTrue(tariff instanceof PremiumTariff);
    }

    @Test
    void createPremiumWrongParamTypes() {
        Map<String, Object> params = new HashMap<>();
        params.put("internationalCalls", "x");
        params.put("roaming", 123);
        params.put("entertainment", "y");

        Tariff tariff = factory.createTariff(
                TariffType.PREMIUM,
                "Premium",
                200,
                params
        );
        assertTrue(tariff instanceof PremiumTariff);
    }

    @Test
    void createPremiumWithNullParams() {
        Tariff tariff = factory.createTariff(
                TariffType.PREMIUM,
                "Premium",
                200,
                null
        );
        assertTrue(tariff instanceof PremiumTariff);
    }

    @Test
    void createBusinessAllParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("roaming", true);
        params.put("prioritySupport", true);
        params.put("dedicatedAccountManager", true);

        Tariff tariff = factory.createTariff(
                TariffType.BUSINESS,
                "Business",
                300,
                params
        );
        assertTrue(tariff instanceof BusinessTariff);
    }

    @Test
    void createBusinessMissingParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("roaming", true);

        Tariff tariff = factory.createTariff(
                TariffType.BUSINESS,
                "Business",
                300,
                params
        );
        assertTrue(tariff instanceof BusinessTariff);
    }

    @Test
    void createBusinessWrongParamTypes() {
        Map<String, Object> params = new HashMap<>();
        params.put("roaming", "x");
        params.put("prioritySupport", 1);
        params.put("dedicatedAccountManager", "y");

        Tariff tariff = factory.createTariff(
                TariffType.BUSINESS,
                "Business",
                300,
                params
        );
        assertTrue(tariff instanceof BusinessTariff);
    }

    @Test
    void createBusinessWithNullParams() {
        Tariff tariff = factory.createTariff(
                TariffType.BUSINESS,
                "Business",
                300,
                null
        );
        assertTrue(tariff instanceof BusinessTariff);
    }

    @Test
    void createFamilyAllParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("members", 4);
        params.put("sharedGB", 40.0);
        params.put("familyDiscountPercent", 15.0);

        Tariff tariff = factory.createTariff(
                TariffType.FAMILY,
                "Family",
                250,
                params
        );
        assertTrue(tariff instanceof FamilyTariff);
    }

    @Test
    void createFamilyPartialParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("members", 3);

        Tariff tariff = factory.createTariff(
                TariffType.FAMILY,
                "Family",
                250,
                params
        );
        assertTrue(tariff instanceof FamilyTariff);
    }

    @Test
    void createFamilyWrongParamTypes() {
        Map<String, Object> params = new HashMap<>();
        params.put("members", "x");
        params.put("sharedGB", "y");
        params.put("familyDiscountPercent", "z");

        Tariff tariff = factory.createTariff(
                TariffType.FAMILY,
                "Family",
                250,
                params
        );
        assertTrue(tariff instanceof FamilyTariff);
    }

    @Test
    void createFamilyWithNullParams() {
        Tariff tariff = factory.createTariff(
                TariffType.FAMILY,
                "Family",
                250,
                null
        );
        assertTrue(tariff instanceof FamilyTariff);
    }

    @Test
    void createStudentAllParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("discountPercent", 20.0);
        params.put("verification", true);

        Tariff tariff = factory.createTariff(
                TariffType.STUDENT,
                "Student",
                80,
                params
        );
        assertTrue(tariff instanceof StudentTariff);
    }

    @Test
    void createStudentOnlyDiscount() {
        Map<String, Object> params = new HashMap<>();
        params.put("discountPercent", 15.0);

        Tariff tariff = factory.createTariff(
                TariffType.STUDENT,
                "Student",
                80,
                params
        );
        assertTrue(tariff instanceof StudentTariff);
    }

    @Test
    void createStudentWrongParamTypes() {
        Map<String, Object> params = new HashMap<>();
        params.put("discountPercent", "x");
        params.put("verification", "y");

        Tariff tariff = factory.createTariff(
                TariffType.STUDENT,
                "Student",
                80,
                params
        );
        assertTrue(tariff instanceof StudentTariff);
    }

    @Test
    void createStudentWithNullParams() {
        Tariff tariff = factory.createTariff(
                TariffType.STUDENT,
                "Student",
                80,
                null
        );
        assertTrue(tariff instanceof StudentTariff);
    }

    @Test
    void fallbackToBasicWhenTypeIsNull() {
        Map<String, Object> params = new HashMap<>();
        params.put("freeMinutes", 10);
        params.put("internetGB", 1.0);

        Tariff tariff = factory.createTariff(
                null,
                "Default",
                50,
                params
        );
        assertTrue(tariff instanceof BasicTariff);
    }

    @Test
    void fallbackToBasicWhenTypeIsNullAndParamsWrong() {
        Map<String, Object> params = new HashMap<>();
        params.put("freeMinutes", "x");
        params.put("internetGB", "y");

        Tariff tariff = factory.createTariff(
                null,
                "Default",
                50,
                params
        );
        assertTrue(tariff instanceof BasicTariff);
    }
}

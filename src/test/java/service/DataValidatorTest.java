package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataValidatorTest {

    private final DataValidator validator = new DataValidator();

    @Test
    void shouldReturnFalseWhenNameIsNull() {
        assertFalse(validator.validateTariff(null, 100));
    }

    @Test
    void shouldReturnFalseWhenNameIsEmpty() {
        assertFalse(validator.validateTariff("   ", 100));
    }

    @Test
    void shouldReturnFalseWhenMonthlyFeeIsNegative() {
        assertFalse(validator.validateTariff("Basic", -10));
    }

    @Test
    void shouldAllowZeroMonthlyFee() {
        assertTrue(validator.validateTariff("Free", 0));
    }

    @Test
    void shouldReturnTrueForValidTariff() {
        assertTrue(validator.validateTariff("Basic", 100));
    }

    @Test
    void shouldReturnFalseWhenMinIsNegative() {
        assertFalse(validator.validatePriceRange(-1, 10));
    }

    @Test
    void shouldReturnFalseWhenMaxIsNegative() {
        assertFalse(validator.validatePriceRange(10, -1));
    }

    @Test
    void shouldReturnFalseWhenMinGreaterThanMax() {
        assertFalse(validator.validatePriceRange(20, 10));
    }

    @Test
    void shouldReturnTrueWhenMinEqualsMax() {
        assertTrue(validator.validatePriceRange(10, 10));
    }

    @Test
    void shouldReturnTrueForValidRange() {
        assertTrue(validator.validatePriceRange(5, 20));
    }
}

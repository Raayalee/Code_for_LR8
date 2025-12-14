package service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Простий валідатор для тарифів / діапазонів
 */
public class DataValidator {

    private static final Logger logger =
            LogManager.getLogger(DataValidator.class);

    public boolean validateTariff(String name, double monthlyFee) {

        if (name == null || name.trim().isEmpty()) {
            logger.warn("Tariff validation failed: empty or null name");
            return false;
        }

        if (monthlyFee < 0) {
            logger.warn("Tariff validation failed: negative monthly fee {}", monthlyFee);
            return false;
        }

        return true;
    }

    public boolean validatePriceRange(double min, double max) {

        if (min < 0 || max < 0) {
            logger.warn(
                    "Price range validation failed: negative values (min={}, max={})",
                    min, max
            );
            return false;
        }

        if (min > max) {
            logger.warn(
                    "Price range validation failed: min > max (min={}, max={})",
                    min, max
            );
            return false;
        }

        return true;
    }
}

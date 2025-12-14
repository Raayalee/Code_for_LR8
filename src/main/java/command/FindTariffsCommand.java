package command;

import model.Tariff;
import service.DataValidator;
import service.SearchService;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FindTariffsCommand implements Command {

    private static final Logger logger =
            LogManager.getLogger(FindTariffsCommand.class);

    private final List<Tariff> tariffs;
    private final SearchService searchService;
    private final DataValidator validator;

    public FindTariffsCommand(List<Tariff> tariffs,
                              SearchService searchService,
                              DataValidator validator) {
        this.tariffs = tariffs;
        this.searchService = searchService;
        this.validator = validator;
    }

    @Override
    public String getDescription() {
        return "Find tariffs in price range. Usage: min max";
    }

    @Override
    public void execute(String parameters) {
        logger.info("FindTariffsCommand started");
        System.out.print(process(parameters));
    }

    public String process(String parameters) {
        try {
            if (parameters == null || parameters.trim().isEmpty()) {
                logger.warn("No parameters provided for tariff search");
                return "Provide min and max price. Example: 100 500\n";
            }

            String[] parts = parameters.trim().split("\\s+");
            if (parts.length < 2) {
                logger.warn("Insufficient parameters for tariff search: '{}'", parameters);
                return "Need two numbers: min max\n";
            }

            double min = Double.parseDouble(parts[0]);
            double max = Double.parseDouble(parts[1]);

            if (!validator.validatePriceRange(min, max)) {
                logger.warn("Invalid price range provided: min={}, max={}", min, max);
                return "Invalid price range.\n";
            }

            List<Tariff> found =
                    searchService.filterByPriceRange(tariffs, min, max);

            if (found.isEmpty()) {
                logger.info(
                        "No tariffs found in price range [{} - {}]",
                        min, max
                );
                return "No tariffs in the given range.\n";
            }

            logger.info(
                    "Found {} tariffs in price range [{} - {}]",
                    found.size(), min, max
            );

            StringBuilder sb = new StringBuilder("Found tariffs:\n");
            for (Tariff t : found) {
                sb.append(t).append("\n");
            }
            return sb.toString();

        } catch (NumberFormatException e) {
            logger.warn("Invalid number format in parameters: '{}'", parameters);
            return "Price values must be numbers.\n";

        } catch (Exception e) {
            // КРИТИЧНА ПОМИЛКА → файл + email
            logger.error("Unexpected error during tariff search", e);
            return "Error while searching. See logs for details.\n";
        }
    }
}

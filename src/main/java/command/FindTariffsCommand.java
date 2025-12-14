package command;

import model.Tariff;
import service.DataValidator;
import service.SearchService;

import java.util.List;

public class FindTariffsCommand implements Command {

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
        System.out.print(process(parameters));
    }

    public String process(String parameters) {
        try {
            if (parameters == null || parameters.trim().isEmpty()) {
                return "Provide min and max price. Example: 100 500\n";
            }

            String[] parts = parameters.trim().split("\\s+");
            if (parts.length < 2) {
                return "Need two numbers: min max\n";
            }

            double min = Double.parseDouble(parts[0]);
            double max = Double.parseDouble(parts[1]);

            if (!validator.validatePriceRange(min, max)) {
                return "Invalid price range.\n";
            }

            List<Tariff> found =
                    searchService.filterByPriceRange(tariffs, min, max);

            if (found.isEmpty()) {
                return "No tariffs in the given range.\n";
            }

            StringBuilder sb = new StringBuilder("Found tariffs:\n");
            for (Tariff t : found) {
                sb.append(t).append("\n");
            }
            return sb.toString();

        } catch (Exception e) {
            return "Error while searching: " + e.getMessage() + "\n";
        }
    }
}

package command;

import command.Command;
import model.Tariff;

import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Сортування тарифів за ціною.
 * parameters: asc | desc
 */
public class SortTariffsCommand implements Command {

    private static final Logger logger =
            LogManager.getLogger(SortTariffsCommand.class);

    private final List<Tariff> tariffs;

    public SortTariffsCommand(List<Tariff> tariffs) {
        this.tariffs = tariffs;
    }

    @Override
    public String getDescription() {
        return "Sort tariffs by price. Usage: asc|desc";
    }

    @Override
    public void execute(String parameters) {

        logger.info("SortTariffsCommand started");

        boolean asc = true;

        if (parameters != null && parameters.trim().equalsIgnoreCase("desc")) {
            asc = false;
        } else if (parameters != null && !parameters.trim().isEmpty()
                && !parameters.trim().equalsIgnoreCase("asc")) {
            logger.warn("Unknown sort parameter '{}', defaulting to ascending", parameters);
        }

        if (tariffs.isEmpty()) {
            logger.info("No tariffs available to sort");
            System.out.println("No tariffs available.");
            return;
        }

        if (asc) {
            tariffs.sort(Comparator.comparingDouble(Tariff::getMonthlyFee));
        } else {
            tariffs.sort(Comparator.comparingDouble(Tariff::getMonthlyFee).reversed());
        }

        logger.info(
                "Tariffs sorted by price in {} order",
                asc ? "ascending" : "descending"
        );

        System.out.println(
                "Tariffs sorted by price " + (asc ? "ascending" : "descending")
        );
    }
}

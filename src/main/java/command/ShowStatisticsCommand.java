package command;

import command.Command;
import model.Tariff;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Показ статистики — мін/макс/середнє/кількість
 */
public class ShowStatisticsCommand implements Command {

    private static final Logger logger =
            LogManager.getLogger(ShowStatisticsCommand.class);

    private final List<Tariff> tariffs;

    public ShowStatisticsCommand(List<Tariff> tariffs) {
        this.tariffs = tariffs;
    }

    @Override
    public String getDescription() {
        return "Show statistics (min, max, avg price, count)";
    }

    @Override
    public void execute(String parameters) {

        logger.info("ShowStatisticsCommand started");

        int count = tariffs.size();
        logger.info("Tariff count: {}", count);

        System.out.println("Tariff count: " + count);

        if (count == 0) {
            logger.warn("No tariffs available. Statistics cannot be calculated.");
            return;
        }

        tariffs.stream()
                .min(Comparator.comparingDouble(Tariff::getMonthlyFee))
                .ifPresent(t -> {
                    logger.info("Minimum price: {} ({})", t.getMonthlyFee(), t.getName());
                    System.out.printf(
                            "Min price: %.2f (%s)%n",
                            t.getMonthlyFee(), t.getName()
                    );
                });

        tariffs.stream()
                .max(Comparator.comparingDouble(Tariff::getMonthlyFee))
                .ifPresent(t -> {
                    logger.info("Maximum price: {} ({})", t.getMonthlyFee(), t.getName());
                    System.out.printf(
                            "Max price: %.2f (%s)%n",
                            t.getMonthlyFee(), t.getName()
                    );
                });

        OptionalDouble avg =
                tariffs.stream().mapToDouble(Tariff::getMonthlyFee).average();

        logger.info("Average price: {}", avg.orElse(0.0));

        System.out.printf(
                "Average price: %.2f%n",
                avg.orElse(0.0)
        );
    }
}

package command;

import command.Command;
import model.Tariff;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Показ статистики — мін/макс/середнє/кількість
 */
public class ShowStatisticsCommand implements Command {
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
        int count = tariffs.size();
        System.out.println("Tariff count: " + count);
        if (count == 0) return;
        tariffs.stream().min(Comparator.comparingDouble(Tariff::getMonthlyFee))
                .ifPresent(t -> System.out.printf("Min price: %.2f (%s)\n", t.getMonthlyFee(), t.getName()));
        tariffs.stream().max(Comparator.comparingDouble(Tariff::getMonthlyFee))
                .ifPresent(t -> System.out.printf("Max price: %.2f (%s)\n", t.getMonthlyFee(), t.getName()));
        OptionalDouble avg = tariffs.stream().mapToDouble(Tariff::getMonthlyFee).average();
        System.out.printf("Average price: %.2f\n", avg.orElse(0.0));
    }
}

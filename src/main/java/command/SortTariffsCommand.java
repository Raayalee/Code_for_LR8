package command;

import command.Command;
import model.Tariff;

import java.util.Comparator;
import java.util.List;

/**
 * Сортування тарифів за ціною.
 * parameters: asc | desc
 */
public class SortTariffsCommand implements Command {
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
        boolean asc = true;
        if (parameters != null && parameters.trim().equalsIgnoreCase("desc")) asc = false;
        if (asc) {
            tariffs.sort(Comparator.comparingDouble(Tariff::getMonthlyFee));
        } else {
            tariffs.sort(Comparator.comparingDouble(Tariff::getMonthlyFee).reversed());
        }
        System.out.println("Tariffs sorted by price " + (asc ? "ascending" : "descending"));
    }
}

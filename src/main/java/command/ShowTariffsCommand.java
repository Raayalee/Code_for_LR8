package command;

import command.Command;
import model.Tariff;

import java.util.List;

/**
 * Показати усі тарифи
 */
public class ShowTariffsCommand implements Command {
    private final List<Tariff> tariffs;

    public ShowTariffsCommand(List<Tariff> tariffs) {
        this.tariffs = tariffs;
    }

    @Override
    public String getDescription() {
        return "Show all tariffs";
    }

    @Override
    public void execute(String parameters) {
        if (tariffs.isEmpty()) {
            System.out.println("No tariffs available.");
            return;
        }
        System.out.println("Tariffs:");
        tariffs.forEach(System.out::println);
    }
}

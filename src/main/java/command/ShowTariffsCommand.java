package command;

import command.Command;
import model.Tariff;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Показати усі тарифи
 */
public class ShowTariffsCommand implements Command {

    private static final Logger logger =
            LogManager.getLogger(ShowTariffsCommand.class);

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

        logger.info("ShowTariffsCommand started");

        if (tariffs.isEmpty()) {
            logger.info("No tariffs available to display");
            System.out.println("No tariffs available.");
            return;
        }

        logger.info("Displaying {} tariffs", tariffs.size());

        System.out.println("Tariffs:");
        tariffs.forEach(System.out::println);
    }
}

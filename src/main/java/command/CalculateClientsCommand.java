package command;

import command.Command;
import model.Client;
import model.Tariff;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Підрахунок клієнтів: загальна кількість та кількість на кожному тарифі
 */
public class CalculateClientsCommand implements Command {

    private static final Logger logger =
            LogManager.getLogger(CalculateClientsCommand.class);

    private final List<Tariff> tariffs;
    private final List<Client> clients;

    public CalculateClientsCommand(List<Tariff> tariffs, List<Client> clients) {
        this.tariffs = tariffs;
        this.clients = clients;
    }

    @Override
    public String getDescription() {
        return "Count the total number of clients and per-tariff statistics";
    }

    @Override
    public void execute(String parameters) {

        logger.info("CalculateClientsCommand started");

        System.out.println("========== CLIENT STATISTICS ==========\n");

        if (tariffs == null || tariffs.isEmpty()) {
            logger.warn("No tariffs found. Statistics calculation aborted.");
            System.out.println("No tariffs found. Cannot calculate statistics.");
            return;
        }

        if (clients == null || clients.isEmpty()) {
            logger.info("No clients found. All tariffs have zero subscribers.");
            System.out.println("No clients found.");
            System.out.println("Tariffs exist, but no one is subscribed yet.\n");
            printTariffsZero();
            return;
        }

        // Загальна кількість клієнтів
        int total = clients.size();
        logger.info("Total number of clients: {}", total);

        System.out.println("Total clients: " + total + "\n");

        // Кількість клієнтів на кожному тарифі
        System.out.println("Clients per tariff:");
        System.out.println("---------------------------------------");

        for (Tariff t : tariffs) {
            System.out.printf(" • %-20s : %d%n", t.getName(), t.getClientCount());
        }

        System.out.println("---------------------------------------");
        System.out.println("=======================================\n");
    }

    private void printTariffsZero() {
        System.out.println("Clients per tariff:");
        System.out.println("---------------------------------------");
        for (Tariff t : tariffs) {
            System.out.printf(" • %-20s : 0%n", t.getName());
        }
        System.out.println("---------------------------------------");
    }
}

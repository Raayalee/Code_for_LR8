package command;

import command.Command;
import model.Client;
import model.Tariff;

import java.util.List;

/**
 * Підрахунок клієнтів: загальна кількість та кількість на кожному тарифі
 */
public class CalculateClientsCommand implements Command {

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

        System.out.println("========== CLIENT STATISTICS ==========\n");

        if (tariffs == null || tariffs.isEmpty()) {
            System.out.println("No tariffs found. Cannot calculate statistics.");
            return;
        }

        if (clients == null || clients.isEmpty()) {
            System.out.println("No clients found.");
            System.out.println("Tariffs exist, but no one is subscribed yet.\n");
            printTariffsZero();
            return;
        }

        // Загальна кількість
        int total = clients.size();
        System.out.println("Total clients: " + total + "\n");

        // Кількість на кожному тарифі
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

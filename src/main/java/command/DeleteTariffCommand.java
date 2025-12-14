package command;

import model.Client;
import model.Tariff;
import model.TariffType;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Видалення тарифу за назвою з підтвердженням.
 * Після видалення всіх клієнтів тарифу переводить на BASIC.
 */
public class DeleteTariffCommand implements Command {

    private static final Logger logger =
            LogManager.getLogger(DeleteTariffCommand.class);

    private final List<Tariff> tariffs;
    private final List<Client> clients;
    private final Scanner scanner;

    public DeleteTariffCommand(List<Tariff> tariffs, List<Client> clients, Scanner scanner) {
        this.tariffs = tariffs;
        this.clients = clients;
        this.scanner = scanner;
    }

    public DeleteTariffCommand(List<Tariff> tariffs, List<Client> clients) {
        this(tariffs, clients, new Scanner(System.in));
    }

    @Override
    public String getDescription() {
        return "Delete tariff by name. Usage: name";
    }

    @Override
    public void execute(String parameters) {

        logger.info("DeleteTariffCommand started");

        if (parameters == null || parameters.trim().isEmpty()) {
            logger.warn("No tariff name provided for deletion");
            System.out.println("Provide tariff name to delete.");
            return;
        }

        String name = parameters.trim();

        // Пошук тарифу
        Optional<Tariff> targetOpt = tariffs.stream()
                .filter(t -> t.getName().equalsIgnoreCase(name))
                .findFirst();

        if (targetOpt.isEmpty()) {
            logger.warn("Tariff not found: {}", name);
            System.out.println("Tariff not found: " + name);
            return;
        }

        Tariff target = targetOpt.get();

        // Підрахунок клієнтів
        long subscriberCount = clients.stream()
                .filter(c -> c.getCurrentTariff() != null &&
                        c.getCurrentTariff().getName().equalsIgnoreCase(name))
                .count();

        logger.info(
                "Tariff '{}' selected for deletion. Subscribers count: {}",
                name, subscriberCount
        );

        System.out.printf("Tariff '%s' selected. Subscribers: %d%n", name, subscriberCount);

        // Підтвердження
        while (true) {
            System.out.print("Are you sure you want to delete this tariff? (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                logger.info("Deletion confirmed for tariff '{}'", name);
                break;
            } else if (input.equals("n") || input.equals("no")) {
                logger.info("Deletion canceled by user for tariff '{}'", name);
                System.out.println("Deletion canceled.");
                return;
            } else {
                logger.warn("Invalid confirmation input: '{}'", input);
                System.out.println("Invalid input. Please type 'y' or 'n'.");
            }
        }

        // Пошук BASIC тарифу
        Optional<Tariff> basicOpt = tariffs.stream()
                .filter(t -> t.getType() == TariffType.BASIC)
                .findFirst();

        if (basicOpt.isEmpty()) {
            // КРИТИЧНА СИТУАЦІЯ → піде на email
            logger.error("No BASIC tariff found. Cannot transfer clients.");
            System.out.println("No BASIC tariff available. Cannot transfer clients.");
            return;
        }

        Tariff basicTariff = basicOpt.get();

        // Переведення клієнтів
        for (Client c : clients) {
            if (c.getCurrentTariff() != null &&
                    c.getCurrentTariff().getName().equalsIgnoreCase(name)) {
                c.subscribeToTariff(basicTariff);
            }
        }

        // Видалення тарифу
        Iterator<Tariff> it = tariffs.iterator();
        while (it.hasNext()) {
            if (it.next().getName().equalsIgnoreCase(name)) {
                it.remove();
                break;
            }
        }

        logger.info(
                "Tariff '{}' deleted successfully. All subscribers moved to BASIC tariff '{}'",
                name, basicTariff.getName()
        );

        System.out.printf(
                "Tariff '%s' deleted. All subscribers moved to BASIC tariff '%s'.%n",
                name, basicTariff.getName()
        );
    }
}

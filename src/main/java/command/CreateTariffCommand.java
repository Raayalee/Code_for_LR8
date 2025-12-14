package command;

import command.Command;
import factory.TariffFactory;
import model.Tariff;
import model.TariffType;
import service.DataValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Створення тарифу у два етапи:
 * 1) name;TYPE;monthlyFee;minutes;internetGB
 * 2) додаткові параметри (для не BASIC тарифів)
 */
public class CreateTariffCommand implements Command {

    private static final Logger logger =
            LogManager.getLogger(CreateTariffCommand.class);

    private final List<Tariff> tariffs;
    private final TariffFactory factory;
    private final DataValidator validator;

    public CreateTariffCommand(List<Tariff> tariffs,
                               TariffFactory factory,
                               DataValidator validator) {
        this.tariffs = tariffs;
        this.factory = factory;
        this.validator = validator;
    }

    @Override
    public String getDescription() {
        return "Create new tariff in two steps";
    }

    @Override
    public void execute(String parameters) {

        logger.info("CreateTariffCommand started");

        if (parameters == null || parameters.trim().isEmpty()) {
            logger.warn("Empty parameters provided");
            System.out.println("Enter: name;TYPE;monthlyFee;minutes;internetGB");
            return;
        }

        try {
            // ===== ЕТАП 1 =====
            String[] parts = parameters.split(";");
            if (parts.length < 5) {
                logger.warn("Invalid parameter format: {}", parameters);
                System.out.println("Invalid format. Use: name;TYPE;monthlyFee;minutes;internetGB");
                return;
            }

            String name = parts[0].trim();
            TariffType type = parseType(parts[1].trim());
            double fee = parseFee(parts[2].trim());
            int minutes = Integer.parseInt(parts[3].trim());
            double internetGB = Double.parseDouble(parts[4].trim());

            if (!validator.validateTariff(name, fee)) {
                logger.warn("Tariff validation failed: name={}, fee={}", name, fee);
                System.out.println("Tariff validation failed. Check name or fee.");
                return;
            }

            Map<String, Object> extraParams = new HashMap<>();
            extraParams.put("freeMinutes", minutes);
            extraParams.put("internetGB", internetGB);

            if (type == TariffType.BASIC) {
                Tariff tariff = factory.createTariff(type, name, fee, extraParams);
                tariffs.add(tariff);

                logger.info("BASIC tariff created successfully: {}", tariff);

                System.out.println("Tariff created successfully:");
                System.out.println(tariff);
                return;
            }

            // ===== ЕТАП 2 =====
            showParameterMenu(type);
            System.out.println("Enter parameters in format: value;value;value");

            Scanner scanner = new Scanner(System.in);
            String paramInput = scanner.nextLine();

            Map<String, Object> stage2Params = parseExtra(type, paramInput);
            extraParams.putAll(stage2Params);

            Tariff tariff = factory.createTariff(type, name, fee, extraParams);
            tariffs.add(tariff);

            logger.info("{} tariff created successfully: {}", type, tariff);

            System.out.println("Tariff created successfully:");
            System.out.println(tariff);

        } catch (Exception e) {
            // КРИТИЧНА ПОМИЛКА → файл + email
            logger.error("Error while creating tariff", e);
            System.out.println("Error creating tariff. See logs for details.");
        }
    }

    private TariffType parseType(String raw) {
        try {
            return TariffType.valueOf(raw.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid tariff type: " + raw);
        }
    }

    private double parseFee(String rawFee) {
        try {
            return Double.parseDouble(rawFee);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Monthly fee must be a number: " + rawFee);
        }
    }

    private void showParameterMenu(TariffType type) {
        System.out.println("\nSelected tariff type: " + type);
        System.out.println("Required additional parameters:");

        switch (type) {
            case PREMIUM -> {
                System.out.println("internationalCalls (boolean)");
                System.out.println("roaming (boolean)");
                System.out.println("entertainment (boolean)");
            }
            case BUSINESS -> {
                System.out.println("roaming (boolean)");
                System.out.println("prioritySupport (boolean)");
                System.out.println("dedicatedAccountManager (boolean)");
            }
            case STUDENT -> {
                System.out.println("discountPercent (double)");
                System.out.println("verification (boolean)");
            }
            case FAMILY -> {
                System.out.println("members (int)");
                System.out.println("sharedGB (double)");
                System.out.println("familyDiscountPercent (double)");
            }
        }
    }

    private Map<String, Object> parseExtra(TariffType type, String raw) {
        Map<String, Object> params = new HashMap<>();
        if (raw == null || raw.trim().isEmpty()) return params;

        String[] items = raw.split(";");

        switch (type) {
            case PREMIUM -> {
                if (items.length >= 1) params.put("internationalCalls", Boolean.parseBoolean(items[0].trim()));
                if (items.length >= 2) params.put("roaming", Boolean.parseBoolean(items[1].trim()));
                if (items.length >= 3) params.put("entertainment", Boolean.parseBoolean(items[2].trim()));
            }
            case BUSINESS -> {
                if (items.length >= 1) params.put("roaming", Boolean.parseBoolean(items[0].trim()));
                if (items.length >= 2) params.put("prioritySupport", Boolean.parseBoolean(items[1].trim()));
                if (items.length >= 3) params.put("dedicatedAccountManager", Boolean.parseBoolean(items[2].trim()));
            }
            case STUDENT -> {
                if (items.length >= 1) params.put("discountPercent", Double.parseDouble(items[0].trim()));
                if (items.length >= 2) params.put("verification", Boolean.parseBoolean(items[1].trim()));
            }
            case FAMILY -> {
                if (items.length >= 1) params.put("members", Integer.parseInt(items[0].trim()));
                if (items.length >= 2) params.put("sharedGB", Double.parseDouble(items[1].trim()));
                if (items.length >= 3) params.put("familyDiscountPercent", Double.parseDouble(items[2].trim()));
            }
        }

        return params;
    }
}

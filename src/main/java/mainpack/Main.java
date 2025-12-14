package mainpack;

import command.*;
import factory.TariffFactory;
import menu.Menu;
import model.Client;
import model.Tariff;
import service.DataValidator;
import service.SearchService;

import java.util.ArrayList;
import java.util.List;

/**
 * Головний клас програми — тепер повністю без початкових даних
 */
public class Main {
    public static void main(String[] args) {
        List<Tariff> tariffs = new ArrayList<>();

        List<Client> clients = new ArrayList<>();

        TariffFactory factory = new TariffFactory();
        DataValidator validator = new DataValidator();
        SearchService searchService = new SearchService();

        // Меню та команди
        Menu mainMenu = new Menu("Mobile tariff management system");

        mainMenu.addCommand("1", new CreateTariffCommand(tariffs, factory, validator));
        mainMenu.addCommand("2", new DeleteTariffCommand(tariffs, clients));
        mainMenu.addCommand("3", new SortTariffsCommand(tariffs));
        mainMenu.addCommand("4", new CalculateClientsCommand(tariffs, clients));
        mainMenu.addCommand("5", new FindTariffsCommand(tariffs, searchService, validator));
        mainMenu.addCommand("6", new ShowTariffsCommand(tariffs));
        mainMenu.addCommand("7", new ShowStatisticsCommand(tariffs));

        System.out.println("=== Available Tariff Types ===");

        System.out.println("BASIC: freeMinutes, internetGB");
        System.out.println("  Example: SuperBasic;BASIC;199.99;freeMinutes=300;internetGB=5.5");

        System.out.println("PREMIUM: internationalCalls, roaming, entertainment");
        System.out.println("  Example: PremiumX;PREMIUM;499.0;internationalCalls=true;roaming=true;entertainment=true");

        System.out.println("BUSINESS: freeInternationalMinutes, prioritySupport, dedicatedAccountManager");
        System.out.println("  Example: BizPro;BUSINESS;899.0;freeInternationalMinutes=300;prioritySupport=true;dedicatedAccountManager=true");

        System.out.println("FAMILY: numberOfFamilyMembers, sharedInternetGB, familyDiscountPercent");
        System.out.println("  Example: FamilyMax;FAMILY;650.0;numberOfFamilyMembers=4;sharedInternetGB=50;familyDiscountPercent=20");

        System.out.println("STUDENT: discountPercent, freeSocialMediaGB, requiresStudentIdVerification");
        System.out.println("  Example: StudyPlus;STUDENT;150.0;discountPercent=30;freeSocialMediaGB=20;requiresStudentIdVerification=true");

        System.out.println("=========================================\n");

        mainMenu.run();
    }
}

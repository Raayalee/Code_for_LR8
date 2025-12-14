package command.impl;

import command.CreateTariffCommand;
import factory.TariffFactory;
import model.Tariff;
import org.junit.jupiter.api.*;

import service.DataValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateTariffCommandTest {

    private List<Tariff> tariffs;
    private TariffFactory factory;
    private DataValidator validator;

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        tariffs = new ArrayList<>();
        factory = new TariffFactory();
        validator = new DataValidator();

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setIn(originalIn);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    // ---------- base guards ----------

    @Test
    void emptyParameters() {
        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("");

        assertTrue(outContent.toString().contains("Enter: name;TYPE;monthlyFee;minutes;internetGB"));
        assertTrue(tariffs.isEmpty());
    }

    @Test
    void invalidFormatLessThanFiveParts() {
        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Basic;BASIC;100");

        assertTrue(outContent.toString().contains("Invalid format"));
        assertTrue(tariffs.isEmpty());
    }

    @Test
    void invalidTariffType() {
        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Basic;WRONG;100;200;10");

        assertTrue(outContent.toString().contains("Error creating tariff"));
        assertTrue(tariffs.isEmpty());
    }

    @Test
    void invalidFee() {
        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Basic;BASIC;abc;200;10");

        assertTrue(outContent.toString().contains("Error creating tariff"));
        assertTrue(tariffs.isEmpty());
    }

    @Test
    void validatorRejectsTariff() {
        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);

        // формат ОК, але fee < 0 => validateTariff = false
        cmd.execute("Bad;BASIC;-10;100;5");

        assertTrue(outContent.toString().contains("Tariff validation failed"));
        assertTrue(tariffs.isEmpty());
    }

    // BASIC шлях

    @Test
    void createBasicTariff() {
        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Basic;BASIC;100;200;10");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    // PREMIUM parseExtra гілки

    @Test
    void createPremiumWithAllThreeValues() {
        System.setIn(new ByteArrayInputStream("true;false;true\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Premium;PREMIUM;150;300;20");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    @Test
    void createPremiumWithOnlyFirstValue() {
        // items.length = 1 => >=1 true, >=2 false, >=3 false
        System.setIn(new ByteArrayInputStream("true\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Premium;PREMIUM;150;300;20");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    @Test
    void createPremiumWithTwoValues() {
        // items.length = 2 => >=1 true, >=2 true, >=3 false
        System.setIn(new ByteArrayInputStream("true;true\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Premium;PREMIUM;150;300;20");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    @Test
    void createPremiumWithEmptySecondStage() {
        // raw = "" (порожній рядок) => parseExtra повертає пусту map
        System.setIn(new ByteArrayInputStream("\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Premium;PREMIUM;150;300;20");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    // BUSINESS parseExtra branches

    @Test
    void createBusinessWithAllThreeValues() {
        System.setIn(new ByteArrayInputStream("true;true;false\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Biz;BUSINESS;500;100;10");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    @Test
    void createBusinessWithOnlyFirstValue() {
        // items.length = 1 => >=1 true, >=2 false, >=3 false
        System.setIn(new ByteArrayInputStream("true\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Biz;BUSINESS;500;100;10");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    @Test
    void createBusinessWithTwoValues() {
        // items.length = 2 => >=1 true, >=2 true, >=3 false
        System.setIn(new ByteArrayInputStream("true;false\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Biz;BUSINESS;500;100;10");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    @Test
    void createBusinessWithEmptySecondStage() {
        System.setIn(new ByteArrayInputStream("\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Biz;BUSINESS;500;100;10");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    // STUDENT parseExtra branches

    @Test
    void createStudentWithTwoValues() {
        System.setIn(new ByteArrayInputStream("15.5;true\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Student;STUDENT;50;100;5");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    @Test
    void createStudentWithOnlyDiscount() {
        // items.length = 1 => >=1 true, >=2 false
        System.setIn(new ByteArrayInputStream("15.5\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Student;STUDENT;50;100;5");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    @Test
    void createStudentWithEmptySecondStage() {
        System.setIn(new ByteArrayInputStream("\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Student;STUDENT;50;100;5");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    // FAMILY parseExtra branches

    @Test
    void createFamilyWithAllThreeValues() {
        System.setIn(new ByteArrayInputStream("4;50;10\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Family;FAMILY;200;500;30");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    @Test
    void createFamilyWithOnlyMembers() {
        // items.length = 1 => >=1 true, >=2 false, >=3 false
        System.setIn(new ByteArrayInputStream("4\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Family;FAMILY;200;500;30");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    @Test
    void createFamilyWithTwoValues() {
        // items.length = 2 => >=1 true, >=2 true, >=3 false
        System.setIn(new ByteArrayInputStream("4;50\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Family;FAMILY;200;500;30");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    @Test
    void createFamilyWithEmptySecondStage() {
        System.setIn(new ByteArrayInputStream("\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Family;FAMILY;200;500;30");

        assertEquals(1, tariffs.size());
        assertTrue(outContent.toString().contains("Tariff created successfully"));
    }

    // catch branch (важкий, але дає справді багато branch)

    @Test
    void shouldGoToCatchWhenSecondStageParamsAreInvalid() {
        // FAMILY очікує int;double;double -> тут буде NumberFormatException
        System.setIn(new ByteArrayInputStream("x;y;z\n".getBytes()));

        CreateTariffCommand cmd = new CreateTariffCommand(tariffs, factory, validator);
        cmd.execute("Family;FAMILY;200;100;10");

        assertTrue(outContent.toString().contains("Error creating tariff"));
        assertTrue(tariffs.isEmpty());
    }
}

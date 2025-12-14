package mainpack;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void shouldRunMainAndExitImmediately() {
        // Підміняємо стандартний ввід: exit
        System.setIn(new ByteArrayInputStream("exit\n".getBytes()));

        // Викликаємо main
        Main.main(new String[]{});

        // Якщо дійшли сюди і не зависли тест успішний
        assertTrue(true);
    }
}

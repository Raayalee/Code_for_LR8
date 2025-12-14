package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTariffTest {

    @Test
    void shouldCreateClient() {
        Client client = new Client("Anna", "123456789");

        assertNotNull(client);
        assertEquals("Anna", client.getName());
        assertEquals("123456789", client.getPhoneNumber());
        assertNull(client.getCurrentTariff());
    }

    @Test
    void shouldSubscribeToTariff() {
        Client client = new Client("Anna", "123456789");
        Tariff tariff = new BasicTariff("Basic", 100, 100, 10);

        client.subscribeToTariff(tariff);

        assertNotNull(client.getCurrentTariff());
        assertEquals(tariff, client.getCurrentTariff());

        // просто викликаємо toString для coverage
        String text = client.toString();
        assertNotNull(text);
    }

    @Test
    void shouldSubscribeNullWhenNoPreviousTariff() {
        Client client = new Client("Anna", "123");

        client.subscribeToTariff(null);

        assertNull(client.getCurrentTariff());
    }

    @Test
    void shouldChangeTariff() {
        Client client = new Client("Anna", "123");
        Tariff first = new BasicTariff("Basic", 100, 100, 10);
        Tariff second = new PremiumTariff("Premium", 200, true, true, true);

        client.subscribeToTariff(first);
        client.subscribeToTariff(second);

        assertEquals(second, client.getCurrentTariff());
    }

    @Test
    void shouldUnsubscribeFromTariff() {
        Client client = new Client("Anna", "123");
        Tariff tariff = new BasicTariff("Basic", 100, 100, 10);

        client.subscribeToTariff(tariff);
        client.subscribeToTariff(null);

        assertNull(client.getCurrentTariff());
    }

    @Test
    void shouldCallToStringWithAndWithoutTariff() {
        Client client = new Client("Anna", "123");

        // currentTariff == null
        String withoutTariff = client.toString();
        assertNotNull(withoutTariff);

        // currentTariff != null
        Tariff tariff = new BasicTariff("Basic", 100, 100, 10);
        client.subscribeToTariff(tariff);

        String withTariff = client.toString();
        assertNotNull(withTariff);
    }
}

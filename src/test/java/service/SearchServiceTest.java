package service;

import model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchServiceTest {

    private final SearchService service = new SearchService();

    @Test
    void shouldFilterTariffsByPriceRange() {
        List<Tariff> tariffs = List.of(
                new BasicTariff("Basic", 100, 100, 10),
                new PremiumTariff("Premium", 200, true, true, true),
                new StudentTariff("Student", 50, 20, true)
        );

        List<Tariff> result = service.filterByPriceRange(tariffs, 80, 150);

        assertEquals(1, result.size());
        assertEquals("Basic", result.get(0).getName());
    }

    @Test
    void shouldFilterTariffsByType() {
        List<Tariff> tariffs = List.of(
                new BasicTariff("Basic", 100, 100, 10),
                new PremiumTariff("Premium", 200, true, true, true),
                new BasicTariff("Basic+", 120, 200, 15)
        );

        List<Tariff> result = service.filterByType(tariffs, TariffType.BASIC);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(t -> t.getType() == TariffType.BASIC));
    }
}

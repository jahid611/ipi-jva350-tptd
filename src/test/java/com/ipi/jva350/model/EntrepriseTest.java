package com.ipi.jva350.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EntrepriseTest {

    @Test
    void testEstDansPlage() {
        LocalDate debut = LocalDate.of(2023, 1, 1);
        LocalDate fin = LocalDate.of(2023, 1, 31);

        assertTrue(Entreprise.estDansPlage(LocalDate.of(2023, 1, 15), debut, fin));
        assertTrue(Entreprise.estDansPlage(LocalDate.of(2023, 1, 1), debut, fin));
        assertTrue(Entreprise.estDansPlage(LocalDate.of(2023, 1, 31), debut, fin));
        
        assertFalse(Entreprise.estDansPlage(LocalDate.of(2022, 12, 31), debut, fin));
        assertFalse(Entreprise.estDansPlage(LocalDate.of(2023, 2, 1), debut, fin));
    }

    @ParameterizedTest
    @CsvSource({
            "2023-01-01, true",
            "2023-05-01, true",
            "2023-07-14, true",
            "2023-12-25, true",
            "2023-04-10, true",
            "2023-01-02, false",
            "2023-05-02, false"
    })
    void testEstJourFerie(String dateStr, boolean attendu) {
        LocalDate date = LocalDate.parse(dateStr);
        assertEquals(attendu, Entreprise.estJourFerie(date));
    }
}
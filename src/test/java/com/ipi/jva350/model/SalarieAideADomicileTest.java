package com.ipi.jva350.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SalarieAideADomicileTest {

    @Test
    void testALegalementDroitADesCongesPayes() {
        // on prépare un salarié vide
        SalarieAideADomicile salarie = new SalarieAideADomicile();

        // 9 jours = pas de congés
        salarie.setJoursTravaillesAnneeN(9);
        assertFalse(salarie.aLegalementDroitADesCongesPayes());

        // 10 jours pile = c'est bon
        salarie.setJoursTravaillesAnneeN(10);
        assertTrue(salarie.aLegalementDroitADesCongesPayes());

        // large au-dessus = c'est bon aussi
        salarie.setJoursTravaillesAnneeN(120);
        assertTrue(salarie.aLegalementDroitADesCongesPayes());
    }

    @ParameterizedTest
    @CsvSource({
            "2022-11-01, 2022-11-05, 5", // semaine normale
            "2022-12-19, 2022-12-26, 6"  // avec un dimanche au milieu qui saute
    })
    void testCalculeJoursDeCongeDecomptesPourPlage(String debutStr, String finStr, int attendu) {
        // on transforme les textes en vraies dates
        LocalDate debut = LocalDate.parse(debutStr);
        LocalDate fin = LocalDate.parse(finStr);
        SalarieAideADomicile salarie = new SalarieAideADomicile();

        // on calcule et on vérifie direct
        int resultat = salarie.calculeJoursDeCongeDecomptesPourPlage(debut, fin);
        assertEquals(attendu, resultat);
    }
}
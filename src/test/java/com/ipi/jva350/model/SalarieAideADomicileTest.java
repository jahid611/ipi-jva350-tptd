package com.ipi.jva350.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

 class SalarieAideADomicileTest {

    @Test
    void testALegalementDroitADesCongesPayes() {
        SalarieAideADomicile salarie = new SalarieAideADomicile();

        salarie.setJoursTravaillesAnneeN(9);
        salarie.setJoursTravaillesAnneeNMoins1(9);
        assertFalse(salarie.aLegalementDroitADesCongesPayes());

        salarie.setJoursTravaillesAnneeN(10);
        salarie.setJoursTravaillesAnneeNMoins1(10);
        assertFalse(salarie.aLegalementDroitADesCongesPayes());

        salarie.setJoursTravaillesAnneeN(120);
        salarie.setJoursTravaillesAnneeNMoins1(120);
        assertTrue(salarie.aLegalementDroitADesCongesPayes());
    }

    @ParameterizedTest
    @CsvSource({
            "2022-11-01, 2022-11-05, 4",
            "2022-12-19, 2022-12-26, 7"
    })
    void testCalculeJoursDeCongeDecomptesPourPlage(String debutStr, String finStr, int attendu) {
        LocalDate debut = LocalDate.parse(debutStr);
        LocalDate fin = LocalDate.parse(finStr);
        SalarieAideADomicile salarie = new SalarieAideADomicile();

        int resultat = salarie.calculeJoursDeCongeDecomptesPourPlage(debut, fin).size();
        
        assertEquals(attendu, resultat);
    }
}
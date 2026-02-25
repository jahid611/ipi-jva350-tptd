package com.ipi.jva350.service;

import com.ipi.jva350.exception.SalarieException;
import com.ipi.jva350.model.SalarieAideADomicile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SalarieAideADomicileServiceIntegrationTest {

    @Autowired
    private SalarieAideADomicileService service;

    @Test
    void testAjouteCongeIntegration() throws SalarieException {
        SalarieAideADomicile salarie = new SalarieAideADomicile();
        salarie.setNom("Paul");
        salarie.setCongesPayesAcquisAnneeNMoins1(15);
        salarie.setCongesPayesPrisAnneeNMoins1(0);
        salarie.setJoursTravaillesAnneeNMoins1(20);
        salarie.setJoursTravaillesAnneeN(20);
        salarie.setMoisEnCours(LocalDate.now());
        salarie.setMoisDebutContrat(LocalDate.now().minusYears(1));
        
        service.creerSalarieAideADomicile(salarie);

        LocalDate debut = LocalDate.now().plusDays(5);
        LocalDate fin = LocalDate.now().plusDays(7);
        service.ajouteConge(salarie, debut, fin);

        int joursPris = salarie.calculeJoursDeCongeDecomptesPourPlage(debut, fin).size();
        assertEquals(joursPris, salarie.getCongesPayesPrisAnneeNMoins1());
    }

    @Test
    void testCalculeLimiteEntrepriseCongesPermisIntegration() {
        LocalDate moisEnCours = LocalDate.of(2023, 12, 1);
        double congesPayesAcquisAnneeNMoins1 = 25.0;
        LocalDate moisDebutContrat = LocalDate.of(2020, 1, 1);
        LocalDate premierJourDeConge = LocalDate.of(2023, 12, 15);
        LocalDate dernierJourDeConge = LocalDate.of(2023, 12, 20);

        long limite = service.calculeLimiteEntrepriseCongesPermis(
                moisEnCours, congesPayesAcquisAnneeNMoins1, moisDebutContrat,
                premierJourDeConge, dernierJourDeConge
        );

        assertTrue(limite > 0);
    }
}
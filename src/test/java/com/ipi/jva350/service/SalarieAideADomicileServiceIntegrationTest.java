package com.ipi.jva350.service;

import com.ipi.jva350.exception.SalarieException;
import com.ipi.jva350.model.SalarieAideADomicile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        salarie.setMoisEnCours(LocalDate.now());
        
        service.creerSalarieAideADomicile(salarie);

        LocalDate debut = LocalDate.now().plusDays(5);
        LocalDate fin = LocalDate.now().plusDays(7);
        service.ajouteConge(salarie, debut, fin);

        int joursPris = salarie.calculeJoursDeCongeDecomptesPourPlage(debut, fin).size();
        assertEquals(joursPris, salarie.getCongesPayesPrisAnneeNMoins1());
    }
}
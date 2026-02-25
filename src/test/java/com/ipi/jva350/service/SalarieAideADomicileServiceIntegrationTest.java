package com.ipi.jva350.service;

import com.ipi.jva350.exception.SalarieException;
import com.ipi.jva350.model.SalarieAideADomicile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

// @SpringBootTest charge toute l'application et la vraie base de données (H2)
@SpringBootTest
public class SalarieAideADomicileServiceIntegrationTest {

    @Autowired
    private SalarieAideADomicileService service;

    @Test
    void testAjouteCongeIntegration() throws SalarieException {
        // on crée un salarié direct en base via la méthode fournie
        SalarieAideADomicile salarie = new SalarieAideADomicile();
        salarie.setNom("Paul");
        salarie.setCongesPayesAcquisAnneeNMoins1(15);
        salarie.setCongesPayesPrisAnneeNMoins1(0);
        salarie.setMoisEnCours(LocalDate.now());
        
        // on l'enregistre via la méthode du service (qui fait des vérifications)
        service.creerSalarieAideADomicile(salarie);

        // on tente de poser 2 jours de congés
        LocalDate debut = LocalDate.now().plusDays(5);
        LocalDate fin = LocalDate.now().plusDays(7);
        service.ajouteConge(salarie, debut, fin);

        // on vérifie que les jours pris ont bien augmenté dans l'objet
        // (La méthode calculeJoursDeCongeDecomptesPourPlage va déduire 2 jours ou plus selon les weekends)
        int joursPris = salarie.calculeJoursDeCongeDecomptesPourPlage(debut, fin);
        assertEquals(joursPris, salarie.getCongesPayesPrisAnneeNMoins1());
    }
}
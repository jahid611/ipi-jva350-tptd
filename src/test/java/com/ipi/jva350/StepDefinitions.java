package com.ipi.jva350;

import com.ipi.jva350.model.SalarieAideADomicile;
import com.ipi.jva350.service.SalarieAideADomicileService;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StepDefinitions {

    @Autowired
    private SalarieAideADomicileService service;

    private SalarieAideADomicile salarie;
    private long limiteCalculee;

    // --- Scénario : Clôture de mois ---

    @Given("un salarié aide à domicile existant avec {int} jours travaillés")
    public void un_salarie_existant(Integer joursInitiaux) {
        salarie = new SalarieAideADomicile();
        salarie.setJoursTravaillesAnneeN(joursInitiaux);
        salarie.setMoisEnCours(LocalDate.of(2026, 1, 1));
    }

    @When("je clôture le mois avec {int} jours travaillés")
    public void je_cloture_le_mois(Integer joursAjoutes) throws Exception {
        service.clotureMois(salarie, joursAjoutes);
    }

    @Then("le salarié a {int} jours travaillés enregistrés")
    public void verif_jours_travailles(Integer totalAttendu) {
        assertEquals(totalAttendu.doubleValue(), salarie.getJoursTravaillesAnneeN());
    }

    // --- Scénario Bonus : Ancienneté ---

    @Given("un salarié avec {int} années d'ancienneté")
    public void un_salarie_avec_anciennete(Integer annees) {
        salarie = new SalarieAideADomicile();
        salarie.setMoisEnCours(LocalDate.of(2026, 1, 1));
        // On recule la date de début de contrat pour créer l'ancienneté
        salarie.setMoisDebutContrat(LocalDate.of(2026 - annees, 1, 1));
        salarie.setCongesPayesAcquisAnneeNMoins1(25);
    }

    @When("on calcule sa limite de congés")
    public void on_calcule_sa_limite() {
        limiteCalculee = service.calculeLimiteEntrepriseCongesPermis(
                salarie.getMoisEnCours(),
                salarie.getCongesPayesAcquisAnneeNMoins1(),
                salarie.getMoisDebutContrat(),
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 15)
        );
    }

    @Then("la limite est de {int} jours")
    public void verif_limite_fixe(Integer attendu) {
        assertEquals(attendu.longValue(), limiteCalculee);
    }

    @Then("la limite est supérieure à {int} jours")
    public void verif_limite_superieure(Integer mini) {
        assertTrue(limiteCalculee > mini);
    }
}
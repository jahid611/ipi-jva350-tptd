package com.ipi.jva350;

import com.ipi.jva350.model.SalarieAideADomicile;
import com.ipi.jva350.service.SalarieAideADomicileService;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest
public class StepDefinitions {

    @Autowired
    private SalarieAideADomicileService service;

    private SalarieAideADomicile salarie;
    private String today;
    private String actualAnswer;
    private long limiteCalculee;

    // --- PARTIE : IS IT FRIDAY (Le test de base) ---
    @Given("today is Sunday")
    public void today_is_sunday() {
        today = "Sunday";
    }

    @When("I ask whether it's Friday yet")
    public void i_ask_whether_it_s_friday_yet() {
        if ("Friday".equals(today)) {
            actualAnswer = "TGIF";
        } else {
            actualAnswer = "Nope";
        }
    }

    @Then("I should be told {string}")
    public void i_should_be_told(String expectedAnswer) {
        assertEquals(expectedAnswer, actualAnswer);
    }

    // --- PARTIE : TP (Clôture de mois & Ancienneté) ---
    @Given("un salarié aide à domicile avec {int} jours travaillés")
    public void un_salarie_aide_a_domicile_avec_jours_travailles(Integer jours) {
        salarie = new SalarieAideADomicile();
        salarie.setNom("Testeur");
        salarie.setJoursTravaillesAnneeN(jours);
        salarie.setMoisEnCours(LocalDate.of(2026, 1, 1));
        salarie.setMoisDebutContrat(LocalDate.of(2025, 1, 1));
    }

    @When("je clôture son mois avec {int} jours de plus")
    public void je_cloture_son_mois_avec_jours_de_plus(Integer nouveauxJours) throws Exception {
        service.clotureMois(salarie, nouveauxJours);
    }

    @Then("le total de ses jours travaillés est {int}")
    public void le_total_de_ses_jours_travailles_est(Integer total) {
        assertEquals(total.doubleValue(), salarie.getJoursTravaillesAnneeN());
    }

    @Given("un salarié avec {int} ans d'ancienneté")
    public void un_salarie_avec_ans_d_anciennete(Integer ans) {
        salarie = new SalarieAideADomicile();
        salarie.setMoisEnCours(LocalDate.of(2026, 1, 1));
        salarie.setMoisDebutContrat(LocalDate.of(2026 - ans, 1, 1));
        salarie.setCongesPayesAcquisAnneeNMoins1(25);
    }

    @When("on calcule sa limite de congés pour l'été")
    public void on_calcule_sa_limite_de_conges_pour_l_ete() {
        limiteCalculee = service.calculeLimiteEntrepriseCongesPermis(
                salarie.getMoisEnCours(), 25, salarie.getMoisDebutContrat(),
                LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 15)
        );
    }

    @Then("la limite doit être supérieure à {int}")
    public void la_limite_doit_etre_superieure_a(Integer valeur) {
        assertTrue(limiteCalculee > valeur);
    }
}
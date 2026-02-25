package com.ipi.jva350;

import com.ipi.jva350.model.SalarieAideADomicile;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {

    private SalarieAideADomicile salarie;

    @Given("un salarié aide à domicile existant avec {int} jours travaillés")
    public void un_salarie_aide_a_domicile_existant_avec_jours_travailles(Integer joursInitiaux) {
        // on initialise le salarié avec les jours du fichier feature
        salarie = new SalarieAideADomicile();
        salarie.setJoursTravaillesAnneeN(joursInitiaux);
    }

    @When("je clôture le mois avec {int} jours travaillés")
    public void je_cloture_le_mois_avec_jours_travailles(Integer joursDuMois) {
        // on ajoute les nouveaux jours de ce mois
        double cumul = salarie.getJoursTravaillesAnneeN() + joursDuMois;
        salarie.setJoursTravaillesAnneeN(cumul);
    }

    @Then("le salarié a {int} jours travaillés enregistrés")
    public void le_salarie_a_jours_travailles_enregistres(Integer attendu) {
        // on check si l'addition est bonne
        assertEquals(attendu.doubleValue(), salarie.getJoursTravaillesAnneeN());
    }
}
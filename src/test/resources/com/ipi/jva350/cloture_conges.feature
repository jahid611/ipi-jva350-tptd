Feature: Gestion des salariés aide à domicile

  Scenario: Clôture du mois d'un salarié
    Given un salarié aide à domicile avec 10 jours travaillés
    When je clôture son mois avec 5 jours de plus
    Then le total de ses jours travaillés est 15

  Scenario: Bonus ancienneté sur les congés
    Given un salarié avec 10 ans d'ancienneté
    When on calcule sa limite de congés pour l'été
    Then la limite doit être supérieure à 10
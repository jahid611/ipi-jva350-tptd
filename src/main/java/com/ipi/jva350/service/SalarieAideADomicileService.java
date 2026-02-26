package com.ipi.jva350.service;

import com.ipi.jva350.exception.SalarieException;
import com.ipi.jva350.model.Entreprise;
import com.ipi.jva350.model.SalarieAideADomicile;
import com.ipi.jva350.repository.SalarieAideADomicileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Service
public class SalarieAideADomicileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalarieAideADomicileService.class);

    @Autowired
    private SalarieAideADomicileRepository salarieAideADomicileRepository;

    public SalarieAideADomicileService() {
    }

    public void creerSalarieAideADomicile(SalarieAideADomicile salarieAideADomicile)
            throws SalarieException, EntityExistsException {
        SalarieAideADomicile existant = salarieAideADomicileRepository.findByNom(salarieAideADomicile.getNom());
        if (existant != null) {
            throw new SalarieException("Un salarié existe déjà avec le nom " + existant.getNom());
        }
        if (salarieAideADomicile.getId() != null) {
            throw new SalarieException("L'id ne doit pas être fourni car il est généré");
        }
        salarieAideADomicileRepository.save(salarieAideADomicile);
    }

    public long calculeLimiteEntrepriseCongesPermis(LocalDate moisEnCours, double congesPayesAcquisAnneeNMoins1,
                                                      LocalDate moisDebutContrat,
                                                      LocalDate premierJourDeConge, LocalDate dernierJourDeConge) {
        double proportionPondereeDuConge = Math.max(Entreprise.proportionPondereeDuMois(premierJourDeConge),
                Entreprise.proportionPondereeDuMois(dernierJourDeConge));
        double limiteConges = proportionPondereeDuConge * congesPayesAcquisAnneeNMoins1;

        Double partCongesPrisTotauxAnneeNMoins1 = salarieAideADomicileRepository.partCongesPrisTotauxAnneeNMoins1();
        if (partCongesPrisTotauxAnneeNMoins1 == null) {
            partCongesPrisTotauxAnneeNMoins1 = 0.0;
        }

        double proportionMoisEnCours = ((premierJourDeConge.getMonthValue()
                - Entreprise.getPremierJourAnneeDeConges(moisEnCours).getMonthValue()) % 12) / 12d;
        double proportionTotauxEnRetardSurLAnnee = proportionMoisEnCours - partCongesPrisTotauxAnneeNMoins1;
        limiteConges += proportionTotauxEnRetardSurLAnnee * 0.2 * congesPayesAcquisAnneeNMoins1;

        int distanceMois = (dernierJourDeConge.getMonthValue() - moisEnCours.getMonthValue()) % 12;
        limiteConges += limiteConges * 0.1 * distanceMois / 12;

        int anciennete = moisEnCours.getYear() - moisDebutContrat.getYear();
        limiteConges += Math.min(anciennete, 10);

        BigDecimal limiteCongesBd = new BigDecimal(Double.toString(limiteConges));
        limiteCongesBd = limiteCongesBd.setScale(3, RoundingMode.HALF_UP);
        return Math.round(limiteCongesBd.doubleValue());
    }

    public void ajouteConge(SalarieAideADomicile salarieAideADomicile, LocalDate jourDebut, LocalDate jourFin)
            throws SalarieException {
        
        LOGGER.info("Demande de congés pour {} du {} au {}", salarieAideADomicile.getNom(), jourDebut, jourFin);

        if (!salarieAideADomicile.aLegalementDroitADesCongesPayes()) {
            LOGGER.warn("Refus : le salarié n'a pas assez de jours travaillés (Annee N-1)");
            throw new SalarieException("N'a pas légalement droit à des congés payés !");
        }

        LinkedHashSet<LocalDate> joursDecomptes = salarieAideADomicile
                .calculeJoursDeCongeDecomptesPourPlage(jourDebut, jourFin);

        if (joursDecomptes.isEmpty()) {
            throw new SalarieException("Pas besoin de congés !");
        }

        LocalDate premierJour = joursDecomptes.stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Aucun jour décompté trouvé"));

        if (premierJour.isBefore(salarieAideADomicile.getMoisEnCours())) {
            throw new SalarieException("Pas possible de prendre de congé avant le mois en cours !");
        }

        int nbCongesPayesPrisDecomptesAnneeN = (int) joursDecomptes.stream()
                .filter(d -> !d.isAfter(LocalDate.of(Entreprise.getPremierJourAnneeDeConges(
                        salarieAideADomicile.getMoisEnCours()).getYear() + 1, 5, 31)))
                .count();

        if (nbCongesPayesPrisDecomptesAnneeN > salarieAideADomicile.getCongesPayesRestantAnneeNMoins1()) {
            throw new SalarieException("Dépassement des congés acquis");
        }

        double limiteEntreprise = this.calculeLimiteEntrepriseCongesPermis(
                salarieAideADomicile.getMoisEnCours(),
                salarieAideADomicile.getCongesPayesAcquisAnneeNMoins1(),
                salarieAideADomicile.getMoisDebutContrat(),
                jourDebut, jourFin);
        
        if (nbCongesPayesPrisDecomptesAnneeN > limiteEntreprise) {
            LOGGER.warn("Refus : dépassement de la limite fixée par l'entreprise ({})", limiteEntreprise);
            throw new SalarieException("Conges Payes Pris Decomptes dépassent la limite de l'entreprise");
        }

        salarieAideADomicile.getCongesPayesPris().addAll(joursDecomptes);
        salarieAideADomicile.setCongesPayesPrisAnneeNMoins1(nbCongesPayesPrisDecomptesAnneeN);

        salarieAideADomicileRepository.save(salarieAideADomicile);
        LOGGER.info("Congés enregistrés avec succès pour {}", salarieAideADomicile.getNom());
    }

    public void clotureMois(SalarieAideADomicile salarieAideADomicile, double joursTravailles) throws SalarieException {
        LOGGER.info("Clôture du mois pour {}", salarieAideADomicile.getNom());
        salarieAideADomicile.setJoursTravaillesAnneeN(salarieAideADomicile.getJoursTravaillesAnneeN() + joursTravailles);
        salarieAideADomicile.setCongesPayesAcquisAnneeN(salarieAideADomicile.getCongesPayesAcquisAnneeN() + 2.5);
        salarieAideADomicile.setMoisEnCours(salarieAideADomicile.getMoisEnCours().plusMonths(1));

        if (salarieAideADomicile.getMoisEnCours().getMonthValue() == 6) {
            clotureAnnee(salarieAideADomicile);
        }
        salarieAideADomicileRepository.save(salarieAideADomicile);
    }

    void clotureAnnee(SalarieAideADomicile salarieAideADomicile) {
        LOGGER.info("Clôture de l'année pour {}", salarieAideADomicile.getNom());
        salarieAideADomicile.setJoursTravaillesAnneeNMoins1(salarieAideADomicile.getJoursTravaillesAnneeN());
        salarieAideADomicile.setCongesPayesAcquisAnneeNMoins1(salarieAideADomicile.getCongesPayesAcquisAnneeN());
        salarieAideADomicile.setCongesPayesPrisAnneeNMoins1(0);
        salarieAideADomicile.setJoursTravaillesAnneeN(0);
        salarieAideADomicile.setCongesPayesAcquisAnneeN(0);
        salarieAideADomicileRepository.save(salarieAideADomicile);
    }
}
package com.ipi.jva350.service;

import com.ipi.jva350.exception.SalarieException;
import com.ipi.jva350.model.SalarieAideADomicile;
import com.ipi.jva350.repository.SalarieAideADomicileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SalarieAideADomicileServiceTest {

    @Mock
    private SalarieAideADomicileRepository repo;

    @InjectMocks
    private SalarieAideADomicileService service;

    @Test
    void testAjouteConge() throws SalarieException {
        SalarieAideADomicile salarie = new SalarieAideADomicile();
        salarie.setCongesPayesAcquisAnneeNMoins1(10);
        salarie.setJoursTravaillesAnneeNMoins1(20);
        salarie.setJoursTravaillesAnneeN(20);
        salarie.setMoisEnCours(LocalDate.now());
        salarie.setMoisDebutContrat(LocalDate.now().minusYears(1));
        
        LocalDate debut = LocalDate.now().plusDays(1);
        LocalDate fin = LocalDate.now().plusDays(3);

        service.ajouteConge(salarie, debut, fin);

        verify(repo, times(1)).save(salarie);
    }

    @Test
    void testCalculeLimiteEntrepriseCongesPermis() {
        when(repo.partCongesPrisTotauxAnneeNMoins1()).thenReturn(0.2);

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
        verify(repo, times(1)).partCongesPrisTotauxAnneeNMoins1();
    }
}
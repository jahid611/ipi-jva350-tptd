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
}
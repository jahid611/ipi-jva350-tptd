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

    // on simule la base de données pour tester juste le service
    @Mock
    private SalarieAideADomicileRepository repo;

    @InjectMocks
    private SalarieAideADomicileService service;

    @Test
    void testAjouteConge() throws SalarieException {
        // on crée qlq avec assez de congés
        SalarieAideADomicile salarie = new SalarieAideADomicile();
        salarie.setCongesPayesAcquisAnneeNMoins1(10);
        
        LocalDate debut = LocalDate.now().plusDays(1);
        LocalDate fin = LocalDate.now().plusDays(3);

        // on tente de lui poser ses congés
        service.ajouteConge(salarie, debut, fin);

        // on vérifie que la sauvegarde a bien été appelée une fois
        verify(repo, times(1)).save(salarie);
    }
}
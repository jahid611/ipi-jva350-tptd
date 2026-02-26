package com.ipi.jva350.repository;

import com.ipi.jva350.model.SalarieAideADomicile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
 class SalarieAideADomicileRepositoryTest {

    @Autowired
    private SalarieAideADomicileRepository repo;

    @Test
    void testFindByNom() {
        SalarieAideADomicile s = new SalarieAideADomicile();
        s.setNom("Jeanne");
        repo.save(s);

        SalarieAideADomicile trouve = repo.findByNom("Jeanne");

        assertNotNull(trouve);
        assertEquals("Jeanne", trouve.getNom());
    }

    @Test
    void testPartCongesPrisTotauxAnneeNMoins1() {
        SalarieAideADomicile s1 = new SalarieAideADomicile();
        s1.setCongesPayesPrisAnneeNMoins1(10);
        repo.save(s1);

        SalarieAideADomicile s2 = new SalarieAideADomicile();
        s2.setCongesPayesPrisAnneeNMoins1(5);
        repo.save(s2);

        Double part = repo.partCongesPrisTotauxAnneeNMoins1();

        assertNotNull(part);
    }
}
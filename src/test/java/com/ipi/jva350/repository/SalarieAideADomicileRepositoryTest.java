package com.ipi.jva350.repository;

import com.ipi.jva350.model.SalarieAideADomicile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

// ça lance une fausse base H2 en mémoire
@DataJpaTest
public class SalarieAideADomicileRepositoryTest {

    @Autowired
    private SalarieAideADomicileRepository repo;

    @Test
    void testFindByNom() {
        // on crée un salarié et on le sauvegarde
        SalarieAideADomicile s = new SalarieAideADomicile();
        s.setNom("Jeanne");
        repo.save(s);

        // on essaie de le retrouver
        SalarieAideADomicile trouve = repo.findByNom("Jeanne");

        // on check si c'est bien elle
        assertNotNull(trouve);
        assertEquals("Jeanne", trouve.getNom());
    }

    @Test
    void testPartCongesPrisTotauxAnneeNMoins1() {
        // on ajoute deux salariés avec des congés pris différents
        SalarieAideADomicile s1 = new SalarieAideADomicile();
        s1.setCongesPayesPrisAnneeNMoins1(10);
        repo.save(s1);

        SalarieAideADomicile s2 = new SalarieAideADomicile();
        s2.setCongesPayesPrisAnneeNMoins1(5);
        repo.save(s2);

        // on lance la requête custom
        Double part = repo.partCongesPrisTotauxAnneeNMoins1();

        // on vérifie que ça a pas planté et que ça renvoie un truc
        assertNotNull(part);
    }
}
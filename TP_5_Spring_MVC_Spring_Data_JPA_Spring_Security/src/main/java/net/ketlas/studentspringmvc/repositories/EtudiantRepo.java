package net.ketlas.studentspringmvc.repositories;

import net.ketlas.studentspringmvc.entities.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtudiantRepo extends JpaRepository<Etudiant,Long> {
    Etudiant findByEmail(String email);
    Page<Etudiant> findByNomContains(String nom, Pageable pageable);
}

package net.ketlas.apjpa.repos;

import net.ketlas.apjpa.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    Patient findByNom(String nom);

}

package net.ketlas.apjpa.services;

import net.ketlas.apjpa.entities.Consultation;
import net.ketlas.apjpa.entities.Medecin;
import net.ketlas.apjpa.entities.Patient;
import net.ketlas.apjpa.entities.RendezVous;

public interface IHospitalService {
    Patient savePatient(Patient patient);
    Medecin saveMedecin(Medecin medecin);
    RendezVous saveRendezVous(RendezVous rendezVous);
    Consultation saveConsultation(Consultation consultation);
}

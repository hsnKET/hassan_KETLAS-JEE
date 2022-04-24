package net.ketlas.patientsmvc.web;

import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.AllArgsConstructor;
import net.ketlas.patientsmvc.entities.Patient;
import net.ketlas.patientsmvc.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {

    private PatientRepository patientRepository;

    @GetMapping("/index")
    public String patients(Model model,
                           @RequestParam(name = "page",defaultValue = "0") int page,
                           @RequestParam(name = "size",defaultValue = "10") int size,
                           @RequestParam(name = "keyword",defaultValue = "") String keyword
                           ){
        Page<Patient> patients = patientRepository.findByNomContains(keyword,PageRequest.of(page,size));
        model.addAttribute("patients",patients.getContent());
        model.addAttribute("pages",new int[patients.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        model.addAttribute("size",size);

        return "patients";
    }

    @GetMapping("/delete")
    public String patients(
                           @RequestParam(name = "id") Long id,
                           @RequestParam(name = "keyword") String keyword,
                           @RequestParam(name = "page",defaultValue = "0") int page,
                           @RequestParam(name = "size",defaultValue = "10") int size
    ){
        patientRepository.deleteById(id);
        return "redirect:/index?page="+page+"&keyword="+keyword+"&size="+size;
    }
    @GetMapping("/editPatient")
    public String editPatient(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "keyword") String keyword,
                              @RequestParam(name = "currentPage",defaultValue = "0") int page,
                              @RequestParam(name = "size",defaultValue = "10") int size,
                              Model model){
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null)
            throw new RuntimeException("No Patient Found Exception!");
        model.addAttribute("patient",patient);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("size",size);
        return "editPatient";
    }
    @GetMapping("/")
    public String home(){
        return "redirect:/index";
    }

    @GetMapping("/addPatient")
    public String addPatient(Model model){
        model.addAttribute("patient",new Patient());
        return "patientsAdd";
    }
    @PostMapping("/save")
    public String save(@Valid Patient patient,
                       BindingResult bindingResult,
                       @RequestParam(name = "keyword" , defaultValue = "") String keyword,
                       @RequestParam(name = "page",defaultValue = "0") int page,
                       @RequestParam(name = "size",defaultValue = "10") int size){
        if (bindingResult.hasErrors())
            return "patientsAdd";
        patientRepository.save(patient);
        return "redirect:/index?page="+page+"&keyword="+keyword+"&size="+size;
    }

    @GetMapping("/all")
    public String all(Model model){
        return "base";
    }
}

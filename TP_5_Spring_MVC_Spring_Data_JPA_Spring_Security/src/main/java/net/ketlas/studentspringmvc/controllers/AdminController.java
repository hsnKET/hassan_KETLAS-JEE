package net.ketlas.studentspringmvc.controllers;

import net.ketlas.studentspringmvc.entities.Etudiant;
import net.ketlas.studentspringmvc.repositories.EtudiantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    EtudiantRepo etudiantRepo;


    @GetMapping("/etudiants/add")
    public String addEtudiant(Model model,
                              @RequestParam(name = "p",defaultValue = "0") int page,
                              @RequestParam(name = "size",defaultValue = "10") int size,
                              @RequestParam(name = "q",defaultValue = "") String q){

        model.addAttribute("etudiant",new Etudiant());
        model.addAttribute("formTitle","Ajouter");

        model.addAttribute("currentPage",page);
        model.addAttribute("size",size);
        model.addAttribute("nom",q);

        return "dashboard/etudiant_add";
    }

    @GetMapping("/etudiants/edit")
    public String editUser(Long id,
                           Model model,
                           @RequestParam(name = "p",defaultValue = "0") int page,
                           @RequestParam(name = "size",defaultValue = "10") int size,
                           @RequestParam(name = "q",defaultValue = "") String q){
        Etudiant etudiant = etudiantRepo.findById(id).orElse(null);
        if (etudiant == null)
            throw new RuntimeException("Etudiant Not Found!");

        model.addAttribute("etudiant",etudiant);
        model.addAttribute("formTitle","Editer");

        model.addAttribute("currentPage",page);
        model.addAttribute("size",size);
        model.addAttribute("nom",q);

        return "dashboard/etudiant_add";
    }



    @DeleteMapping("/etudiants/delete")
    @ResponseBody
    public String delete(Long id,
                           Model model,
                           @RequestParam(name = "p",defaultValue = "0") int page,
                           @RequestParam(name = "size",defaultValue = "10") int size,
                           @RequestParam(name = "q",defaultValue = "") String q,
                            RedirectAttributes redirAttrs){

        Etudiant etudiant = etudiantRepo.findById(id).orElse(null);
        if (etudiant == null)
            throw new RuntimeException("Etudiant Not Found!");
        etudiantRepo.delete(etudiant);
        redirAttrs.addFlashAttribute("error", "Etudiant est supprimé");
//        return "redirect:/user/etudiants?p="+page+"&size="+size+"&q="+q;
        return "{}";

    }

    @PostMapping("/etudiants/save")
    public String saveUser(@RequestParam(name = "p",defaultValue = "") int page,
                           @RequestParam(name = "size",defaultValue = "") int size,
                           @RequestParam(name = "q",defaultValue = "") String nom,
                           @Valid Etudiant etudiant,
                           BindingResult result,
                           RedirectAttributes redirAttrs){
        String msg = etudiant.getId() == null ? "Etudiant bien ajouter":"Etudiant est Mise à jour";
        if (result.hasErrors())
            return "dashboard/etudiant_add";
        etudiantRepo.save(etudiant);
        redirAttrs.addFlashAttribute("success", msg);
        return "redirect:/user/etudiants?p="+page+"&size="+size+"&q="+nom;
    }


}

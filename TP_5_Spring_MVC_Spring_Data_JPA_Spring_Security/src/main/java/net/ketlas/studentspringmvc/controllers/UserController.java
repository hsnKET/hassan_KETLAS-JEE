package net.ketlas.studentspringmvc.controllers;

import net.ketlas.studentspringmvc.entities.Etudiant;
import net.ketlas.studentspringmvc.repositories.EtudiantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    EtudiantRepo etudiantRepo;

    @GetMapping("/etudiants")
    public String users(Model model,
                        @RequestParam(name = "p",defaultValue = "0") int page,
                        @RequestParam(name = "size",defaultValue = "10") int size,
                        @RequestParam(name = "q",defaultValue = "") String q){

        Page<Etudiant> etudiants = etudiantRepo.findByNomContains(q, PageRequest.of(page,size));

        model.addAttribute("etudiants",etudiants);

        model.addAttribute("pages",new int[etudiants.getTotalPages()]);
        model.addAttribute("totalPage",etudiants.getTotalPages());
        model.addAttribute("pages",new int[etudiants.getTotalPages()]);

        model.addAttribute("currentPage",page);
        model.addAttribute("size",size);
        model.addAttribute("nom",q);

        return "dashboard/etudiants";
    }


}

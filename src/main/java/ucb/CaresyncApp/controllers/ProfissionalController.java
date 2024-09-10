package ucb.CaresyncApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ucb.CaresyncApp.entities.Profissional;
import ucb.CaresyncApp.repositories.ProfissionalRepository;

import java.util.List;

@Controller
@RequestMapping("/profissional")
public class ProfissionalController {

    private ProfissionalRepository repository;

    public ProfissionalController(ProfissionalRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/listar")
    public String listarProfissionais(Model model) {
        List<Profissional> profissionais = repository.findAll();
        model.addAttribute("profissionais", profissionais);
       return "profissional/listar";
    }

    @GetMapping("/formulario-criacao")
    public String mostrarFormularioCriacao(Model model) {
        var profissional = new Profissional();
        model.addAttribute("profissional", profissional);
        return "profissional/criar";
    }

    @PostMapping("/criar")
    public String criarProfissional(@ModelAttribute("profissional") Profissional profissional) {
        repository.save(profissional);
        return "redirect:/profissional/listar";
    }


    @GetMapping("/formulario-edicao/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        Profissional profissional = repository.getReferenceById(id);
        model.addAttribute("profissional", profissional);
        return "profissional/editar";
    }

    @PostMapping("/editar")
    public String editarProfissional(@ModelAttribute("profissional") Profissional profissional) {
        repository.save(profissional);
        return "redirect:/profissional/listar";
    }

    @PostMapping("/excluir/{id}")
    public String excluirProfissional(@PathVariable Long id) {
        var profissionalDeletado = repository.getReferenceById(id);
        repository.delete(profissionalDeletado);
        return "redirect:/profissional/listar";
    }

}

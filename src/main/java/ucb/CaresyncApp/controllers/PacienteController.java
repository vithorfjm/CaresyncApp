package ucb.CaresyncApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ucb.CaresyncApp.entities.Paciente;
import ucb.CaresyncApp.repositories.PacienteRepository;

import java.util.List;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    private PacienteRepository repository;

    public PacienteController(PacienteRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/listar")
    public String listarPacientes(Model model) {
        List<Paciente> pacientes = repository.findAll();
        model.addAttribute("pacientes", pacientes);
        return "paciente/listar";
    }

    @GetMapping("/formulario-criacao")
    public String mostrarFormularioCriacao(Model model) {
        var paciente = new Paciente();
        model.addAttribute("paciente", paciente);
        return "paciente/criar";
    }

    @PostMapping("/criar")
    public String criarPaciente(@ModelAttribute("paciente") Paciente paciente) {
        repository.save(paciente);
        return "redirect:/paciente/listar";
    }


    @GetMapping("/formulario-edicao/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        Paciente paciente = repository.getReferenceById(id);
        model.addAttribute("paciente", paciente);
        return "paciente/editar";
    }

    @PostMapping("/editar")
    public String editarPaciente(@ModelAttribute("paciente") Paciente paciente) {
        repository.save(paciente);
        return "redirect:/paciente/listar";
    }

    @PostMapping("/excluir/{id}")
    public String excluirPaciente(@PathVariable Long id) {
        var pacienteDeletado = repository.getReferenceById(id);
        repository.delete(pacienteDeletado);
        return "redirect:/paciente/listar";
    }

}

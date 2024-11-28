package ucb.CaresyncApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ucb.CaresyncApp.DTOs.VacinaResponseDTO;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.entities.Vacina;
import ucb.CaresyncApp.services.VacinaService;

import java.util.List;

@RestController
@RequestMapping("/vacina")
public class VacinaController {

    @Autowired
    private VacinaService vacinaService;

    @GetMapping("/listar-vacinas")
    public List<VacinaResponseDTO> listarVacinasDoPaciente(@AuthenticationPrincipal User user) {
        return vacinaService.listarVacinasDoPaciente(user);
    }

    @GetMapping("/listar-vacinas/{id}")
    public VacinaResponseDTO listarVacinasDoPaciente(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return vacinaService.listarVacinaPeloId(id, user);
    }

}

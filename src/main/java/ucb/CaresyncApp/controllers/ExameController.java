package ucb.CaresyncApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ucb.CaresyncApp.DTOs.ConsultaResponseDTO;
import ucb.CaresyncApp.DTOs.ExameResponseDTO;
import ucb.CaresyncApp.DTOs.MarcacaoExameDTO;
import ucb.CaresyncApp.entities.Exame;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.repositories.ExameRepository;
import ucb.CaresyncApp.services.ExameService;

import java.util.List;

@RestController
@RequestMapping("/exame")
public class ExameController {

    @Autowired
    private ExameService exameService;

    @PostMapping("/agendar")
    public ResponseEntity<ExameResponseDTO> agendarExame(@AuthenticationPrincipal User user, @RequestBody @Valid MarcacaoExameDTO exame) {
        var novoExame = exameService.salvarExame(exame, user);
        return ResponseEntity.ok(novoExame);
    }

    @GetMapping("/listar-exames")
    public ResponseEntity<List<ExameResponseDTO>> listarExames(@AuthenticationPrincipal User user) {
        var listaExames = exameService.listarExamesDoUsuario(user);
        return ResponseEntity.ok().body(listaExames);
    }

    @GetMapping("/listar-exames/{id}")
    public ResponseEntity<ExameResponseDTO> listarExamesPeloId(@AuthenticationPrincipal User user, @PathVariable Long id) {
        var exame = exameService.listarExamePeloId(id, user);
        return ResponseEntity.ok().body(exame);
    }

}

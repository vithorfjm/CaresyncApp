package ucb.CaresyncApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import ucb.CaresyncApp.DTOs.ConsultaResponseDTO;
import ucb.CaresyncApp.DTOs.MarcacaoConsultaDTO;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.repositories.ConsultaRepository;
import ucb.CaresyncApp.services.ConsultaService;

import java.util.List;

@RestController
@RequestMapping("/consulta")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;
    private ConsultaRepository consultaRepository;

    @PostMapping("/teste")
    public String teste(@AuthenticationPrincipal User user) {
        System.out.println(user.getFirstName() + " " + user.getLastName() + " " + user.getId());
        return user.getEmail();
    }

    @PostMapping("/agendar")
    public ResponseEntity<ConsultaResponseDTO> marcarConsulta(@AuthenticationPrincipal User user, @RequestBody @Valid MarcacaoConsultaDTO consulta) {
        var novaConsulta = consultaService.marcarConsulta(consulta, user);
        return ResponseEntity.ok(novaConsulta);
    }

    @GetMapping("/listar-consultas")
    public ResponseEntity<List<ConsultaResponseDTO>> listarConsultasDoPaciente(@AuthenticationPrincipal User user) {
        var listaConsultas = consultaService.listarConsultasPorPaciente(user);
        return ResponseEntity.ok().body(listaConsultas);
    }

    @GetMapping("/listar-consultas/{id}")
    public ResponseEntity<ConsultaResponseDTO> listarConsultaPeloId(@AuthenticationPrincipal User user, @PathVariable Long id) {
        var consulta = consultaService.listarConsultaPeloId(id, user);
        return ResponseEntity.ok().body(consulta);
    }

}

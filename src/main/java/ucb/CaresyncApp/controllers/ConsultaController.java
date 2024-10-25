package ucb.CaresyncApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ucb.CaresyncApp.DTOs.MarcacaoConsultaDTO;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.services.ConsultaService;

@RestController
@RequestMapping("/consulta")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @PostMapping("/teste")
    public String teste(@AuthenticationPrincipal User user) {
        System.out.println(user.getFirstName() + " " + user.getLastName() + " " + user.getId());
        return user.getEmail();
    }

    @PostMapping("/marcar")
    public ResponseEntity marcarConsulta(@AuthenticationPrincipal User user, @RequestBody @Valid MarcacaoConsultaDTO consulta) {
        var novaConsulta = consultaService.marcarConsulta(consulta, user);
        return ResponseEntity.ok(novaConsulta);

    }

}

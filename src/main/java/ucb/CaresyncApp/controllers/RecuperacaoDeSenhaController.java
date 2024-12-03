package ucb.CaresyncApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ucb.CaresyncApp.DTOs.recuperacao_de_senha.CodigoEmailRequestDTO;
import ucb.CaresyncApp.DTOs.recuperacao_de_senha.CodigoValidoResponseDTO;
import ucb.CaresyncApp.DTOs.recuperacao_de_senha.AlterarSenhaRequestDTO;
import ucb.CaresyncApp.DTOs.recuperacao_de_senha.EmailRequestDTO;
import ucb.CaresyncApp.services.RecuperacaoDeSenhaService;
import ucb.CaresyncApp.services.UserService;

@RestController
@RequestMapping("")
public class RecuperacaoDeSenhaController {

    @Autowired
    private RecuperacaoDeSenhaService recuperacaoDeSenhaService;
    @Autowired
    private UserService userService;

    @PostMapping("/esqueci-a-senha")
    public ResponseEntity<Void> enviarEmailComCodigo(@RequestBody @Valid EmailRequestDTO dados) {
        recuperacaoDeSenhaService.enviarEmail(dados.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/codigo")
    public ResponseEntity<CodigoValidoResponseDTO> validarCodigoRecebido(@RequestBody @Valid CodigoEmailRequestDTO dados) {
        var codigoValido = recuperacaoDeSenhaService.validarCodigo(dados.email(), dados.codigo());
        return ResponseEntity.ok().body(codigoValido);
    }

    @PostMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid AlterarSenhaRequestDTO dados) {
        recuperacaoDeSenhaService.alterarSenha(dados);
        return ResponseEntity.ok().build();
    }
}

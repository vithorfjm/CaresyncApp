package ucb.CaresyncApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ucb.CaresyncApp.DTOs.LoginRequestDTO;
import ucb.CaresyncApp.DTOs.CadastroRequestDTO;
import ucb.CaresyncApp.DTOs.TokenReponseDTO;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.services.TokenService;
import ucb.CaresyncApp.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestDTO data) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenReponseDTO(tokenJWT));
    }

    @PostMapping("/cadastro")
    public ResponseEntity register(@RequestBody @Valid CadastroRequestDTO data) {
        userService.criarUsuario(data);
        return ResponseEntity.ok().build();
    }


}

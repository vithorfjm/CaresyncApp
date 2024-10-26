package ucb.CaresyncApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ucb.CaresyncApp.DTOs.EdicaoUserRequestDTO;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping
    public ResponseEntity updateUser(@RequestBody EdicaoUserRequestDTO dados, @AuthenticationPrincipal User user) {
        userService.editarUsuario(user, dados);
        return ResponseEntity.ok().build();
    }

}

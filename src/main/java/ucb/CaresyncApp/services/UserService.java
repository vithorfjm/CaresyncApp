package ucb.CaresyncApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ucb.CaresyncApp.DTOs.CadastroRequestDTO;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listarTodosOsMedicos () {
        return repository.findAll();
    }

    public List<User> listarMedicosPorEspecialidade(String especialidade) {
        return repository.findAll();
    }

    public boolean editarUsuario(User user) {
        return true;
    }

    public void criarUsuario(CadastroRequestDTO dados) {
        User newUser = new User();
        newUser.setAdmin(false);
        newUser.setFirstName(dados.nome());
        newUser.setLastName(dados.sobrenome());
        newUser.setEmail(dados.email());
        newUser.setPassword(passwordEncoder.encode(dados.senha()));
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        newUser.setRole("patient");
        newUser.setSexo(dados.sexo());
        newUser.setDataNascimento(dados.dataNascimento());
        newUser.setCPF(dados.CPF());
        newUser.setEndereco(dados.endereco());
        newUser.setCEP(dados.CEP());
        newUser.setCidade(dados.cidade());
        newUser.setUF(dados.UF());
        newUser.setTelefone(dados.telefone());
        repository.save(newUser);
    }

    public User listarUsuarioPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

}

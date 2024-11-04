package ucb.CaresyncApp.services;

import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ucb.CaresyncApp.DTOs.CadastroRequestDTO;
import ucb.CaresyncApp.DTOs.EdicaoUserRequestDTO;
import ucb.CaresyncApp.DTOs.UsuarioResponseDTO;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.exceptions.custom.MedicoIndisponivelException;
import ucb.CaresyncApp.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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

    public void editarUsuario(User usuarioAtual, EdicaoUserRequestDTO novosDados) {
        User usuarioAtualizado = validarDadosEdicao(usuarioAtual, novosDados);
        repository.save(usuarioAtualizado);
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
        newUser.setNumeroSUS(dados.numeroSUS());
        repository.save(newUser);
    }

    public User listarUsuarioPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    private User validarDadosEdicao(User user, EdicaoUserRequestDTO dados) {
        if (dados.email() != null && !dados.email().isEmpty())
            user.setEmail(dados.email());
        if (dados.CEP() != null && !dados.CEP().isEmpty())
            user.setCEP(dados.CEP());
        if (dados.endereco() != null && !dados.endereco().isEmpty())
            user.setEndereco(dados.endereco());
        if (dados.cidade() != null && !dados.cidade().isEmpty())
            user.setCidade(dados.cidade());
        if (dados.UF() != null && !dados.UF().isEmpty())
            user.setUF(dados.UF());
        if (dados.telefone() != null && !dados.telefone().isEmpty())
            user.setTelefone(dados.telefone());
        return user;
    }

    public UsuarioResponseDTO listarUsuarioPeloToken(User user) {
        return new UsuarioResponseDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCPF(),
                user.getSexo(),
                user.getTelefone(),
                user.getCEP(),
                user.getEndereco(),
                user.getCidade(),
                user.getUF(),
                user.getDataNascimento(),
                user.getNumeroSUS()
        );
    }

    public User listarMedicoALeatorioPelaEspecialidade(String especialidade) {
        var medicos = repository.findByEspecialidade(especialidade);

        if (medicos.isEmpty())
            throw new MedicoIndisponivelException(especialidade);

        Random r = new Random();
        return medicos.get(r.nextInt(medicos.size()));
    }
}

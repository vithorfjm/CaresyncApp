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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void editarUsuario(User usuarioAtual, EdicaoUserRequestDTO novosDados) {
        User usuarioAtualizado = validarDadosEdicao(usuarioAtual, novosDados);
        repository.save(usuarioAtualizado);
    }

    public void criarUsuario(CadastroRequestDTO dados) {
        User novoUsuario = new User(dados, passwordEncoder);
        repository.save(novoUsuario);
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
        return new UsuarioResponseDTO(user);
    }

    public User listarMedicoAleatorioPelaEspecialidadeParaConsulta(String especialidade, LocalDateTime data) {
        var medicos = repository.findMedicosDisponiveisParaConsultaPorEspecialidadeEData(especialidade, data.minusMinutes(19), data.plusMinutes(19));
        if (medicos.isEmpty())
            throw new MedicoIndisponivelException(especialidade);

        Random r = new Random();
        return medicos.get(r.nextInt(medicos.size()));
    }

    public User listarMedicoAleatorioPelaDataParaExame(LocalDateTime data) {
        var medicos = repository.findMedicosDisponiveisParaExamePorData(data.minusMinutes(19), data.plusMinutes(19));
        if (medicos.isEmpty())
            throw new MedicoIndisponivelException();

        Random r = new Random();
        return medicos.get(r.nextInt(medicos.size()));
    }
}

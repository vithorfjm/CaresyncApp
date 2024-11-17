package ucb.CaresyncApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucb.CaresyncApp.DTOs.ConsultaResponseDTO;
import ucb.CaresyncApp.DTOs.ExameResponseDTO;
import ucb.CaresyncApp.DTOs.MarcacaoExameDTO;
import ucb.CaresyncApp.entities.Consulta;
import ucb.CaresyncApp.entities.Exame;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.exceptions.custom.ConsultaNaoEncontradaException;
import ucb.CaresyncApp.exceptions.custom.ConsultaNaoPertenceAoPacienteException;
import ucb.CaresyncApp.exceptions.custom.DataForaDoLimiteException;
import ucb.CaresyncApp.repositories.ExameRepository;
import ucb.CaresyncApp.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExameService {

    @Autowired
    private UserService userService;

    @Autowired
    private ExameRepository exameRepository;

    public ExameResponseDTO salvarExame(MarcacaoExameDTO dados, User paciente) {

        if (dados.data().isAfter(LocalDate.now().plusMonths(3))) {
            throw new DataForaDoLimiteException();
        }

        if (dados.hora().isBefore(LocalTime.of(9, 0)) || dados.hora().isAfter(LocalTime.of(20, 00))) {
            throw new DataForaDoLimiteException("A hora selecionada deve estar entre 09:00 e 20:00.");
        }

        var medico = userService.listarMedicoAleatorioPelaDataParaExame(dados.data().atTime(dados.hora()));

        Exame novoExame = new Exame(dados, paciente, medico);
        exameRepository.save(novoExame);
        return null;
    }

    public List<ExameResponseDTO> listarExamesDoUsuario(User user) {
        List<Exame> exames = exameRepository.findByPacienteId(user.getId());
        List<ExameResponseDTO> listaResponse = new ArrayList<>();
        for (Exame exame : exames) {
            var response = new ExameResponseDTO(exame);
            listaResponse.add(response);
        }
        return listaResponse;
    }

    public ExameResponseDTO listarExamePeloId(Long id, User user) {
        var exame = exameRepository.findById(id).orElseThrow(() -> new ConsultaNaoEncontradaException("Não existe exame cadastrado com o id " + id));

        if (exame.getPaciente().getId() != user.getId()) {
            throw new ConsultaNaoPertenceAoPacienteException("O exame de id " + id + " não pertence ao paciente " + user.getFirstName() + " " + user.getLastName());
        }

        return new ExameResponseDTO(exame);
    }
}

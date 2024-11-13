package ucb.CaresyncApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucb.CaresyncApp.DTOs.ConsultaResponseDTO;
import ucb.CaresyncApp.DTOs.MarcacaoConsultaDTO;
import ucb.CaresyncApp.entities.Consulta;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.exceptions.custom.ConsultaNaoEncontradaException;
import ucb.CaresyncApp.exceptions.custom.ConsultaNaoPertenceAoPacienteException;
import ucb.CaresyncApp.exceptions.custom.DataForaDoLimiteException;
import ucb.CaresyncApp.repositories.ConsultaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository repository;

    @Autowired
    private UserService userService;

    public ConsultaResponseDTO marcarConsulta(MarcacaoConsultaDTO dados, User paciente) {

        if (dados.dataConsulta().isAfter(LocalDate.now().plusMonths(3))) {
            throw new DataForaDoLimiteException();
        }

        if (dados.hora().isBefore(LocalTime.of(9, 0)) || dados.hora().isAfter(LocalTime.of(20, 00))) {
            throw new DataForaDoLimiteException("A hora selecionada deve estar entre 09:00 e 20:00.");
        }

        var medico = userService.listarMedicoALeatorioPelaEspecialidade(dados.especialidade(), dados.dataConsulta().atTime(dados.hora()));

        Consulta novaConsulta = new Consulta(dados, paciente, medico);
        repository.save(novaConsulta);
        return new ConsultaResponseDTO(novaConsulta);
    }

    public boolean confirmarConsulta() {
        return true;
    }

    public boolean cancelarConsulta() {
        return true;
    }

    public boolean concluirConsulta() {
        return true;
    }

    public List<ConsultaResponseDTO> listarConsultasPorPaciente(User user) {
        List<Consulta> consultas = repository.findConsultaByPacienteId(user.getId());
        List<ConsultaResponseDTO> listaResponse = new ArrayList<>();
        for (Consulta consulta : consultas) {
            var response = new ConsultaResponseDTO(consulta);
            listaResponse.add(response);
        }
        return listaResponse;
    }

    public ConsultaResponseDTO listarConsultaPeloId(Long id, User user) {
        var consulta = repository.findById(id).orElseThrow(() -> new ConsultaNaoEncontradaException("Não existe consulta cadastrada com o id " + id));

        if (consulta.getPaciente().getId() != user.getId()) {
            throw new ConsultaNaoPertenceAoPacienteException("A consulta de id " + id + " não pertence ao paciente " + user.getFirstName() + " " + user.getLastName());
        }

        return new ConsultaResponseDTO(consulta);
    }

}

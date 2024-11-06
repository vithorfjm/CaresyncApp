package ucb.CaresyncApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucb.CaresyncApp.DTOs.ConsultaResponseDTO;
import ucb.CaresyncApp.DTOs.MarcacaoConsultaDTO;
import ucb.CaresyncApp.entities.Consulta;
import ucb.CaresyncApp.entities.User;
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

        if (dados.data().isAfter(LocalDate.now().plusMonths(3))) {
            throw new DataForaDoLimiteException();
        }

        if (dados.hora().isBefore(LocalTime.of(9, 0)) || dados.hora().isAfter(LocalTime.of(20, 00))) {
            throw new DataForaDoLimiteException("A hora selecionada deve estar entre 09:00 e 20:00.");
        }

        var medico = userService.listarMedicoALeatorioPelaEspecialidade(dados.especialidade());

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

    public List<Consulta> listarConsultasPorMedicoId(Long id) {
        return null;
    }
}

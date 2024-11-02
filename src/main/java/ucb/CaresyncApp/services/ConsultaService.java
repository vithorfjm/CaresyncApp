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

        Consulta novaConsulta = new Consulta();
        novaConsulta.setDataConsulta(dados.data().atTime(dados.hora()));
        novaConsulta.setPaciente(paciente);
        novaConsulta.setMedico(medico);
        novaConsulta.setStatus("Agendada");
        novaConsulta.setEspecialidade(dados.especialidade());
        novaConsulta.setLocal(dados.local());
        novaConsulta.setEndereco(dados.endereco());
        novaConsulta.setTipo(dados.tipo());
        novaConsulta.setObservacoes(dados.observacoes());
        repository.save(novaConsulta);

        var response = new ConsultaResponseDTO(
                paciente.getFirstName() + " " + paciente.getLastName(),
                medico.getFirstName() + " " + medico.getLastName(),
                dados.data().atTime(dados.hora()),
                "Agendada",
                dados.tipo(),
                dados.especialidade(),
                dados.local(),
                dados.endereco(),
                dados.observacoes());
        return response;
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

    public List<Consulta> listarConsultasPorPacienteId(Long id) {
        return null;
    }

    public List<Consulta> listarConsultasPorMedicoId(Long id) {
        return null;
    }
}

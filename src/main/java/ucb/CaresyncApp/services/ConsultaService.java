package ucb.CaresyncApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucb.CaresyncApp.DTOs.ConsultaResponseDTO;
import ucb.CaresyncApp.DTOs.MarcacaoConsultaDTO;
import ucb.CaresyncApp.entities.Consulta;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.repositories.ConsultaRepository;

import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository repository;

    @Autowired
    private UserService userService;

    public ConsultaResponseDTO marcarConsulta(MarcacaoConsultaDTO dados, User paciente) {
        var medico = userService.listarUsuarioPorId(dados.medicoId());

        Consulta novaConsulta = new Consulta();
        novaConsulta.setDataConsulta(dados.data());
        novaConsulta.setPaciente(paciente);
        novaConsulta.setMedico(medico);
        novaConsulta.setStatus("Agendada");
        novaConsulta.setEspecialidade(dados.especialidade());
        novaConsulta.setLocal(dados.especialidade());
        novaConsulta.setEndereco(dados.endereco());
        novaConsulta.setTipo(dados.tipo());
        novaConsulta.setObservacoes(dados.observacoes());
        repository.save(novaConsulta);

        var response = new ConsultaResponseDTO(
                paciente.getFirstName() + " " + paciente.getLastName(),
                medico.getFirstName() + " " + medico.getLastName(),
                dados.data(),
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

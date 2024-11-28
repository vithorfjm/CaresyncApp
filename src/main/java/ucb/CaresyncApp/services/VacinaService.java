package ucb.CaresyncApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucb.CaresyncApp.DTOs.ConsultaResponseDTO;
import ucb.CaresyncApp.DTOs.VacinaResponseDTO;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.entities.Vacina;
import ucb.CaresyncApp.exceptions.custom.ConsultaNaoEncontradaException;
import ucb.CaresyncApp.exceptions.custom.ConsultaNaoPertenceAoPacienteException;
import ucb.CaresyncApp.repositories.VacinaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class VacinaService {

    @Autowired
    private VacinaRepository vacinaRepository;

    public List<VacinaResponseDTO> listarVacinasDoPaciente(User paciente) {
        var vacinas = vacinaRepository.findByPacienteId(paciente.getId());
        List<VacinaResponseDTO> resposta = new ArrayList<>();
        if (vacinas.isEmpty())
            return resposta;
        for (Vacina vacina : vacinas) {
            resposta.add(new VacinaResponseDTO(vacina));
        }

        return resposta;
    }

    public VacinaResponseDTO listarVacinaPeloId(Long id, User paciente) {
        var vacina = vacinaRepository.findById(id).orElseThrow(() -> new ConsultaNaoEncontradaException("Não existe vacina cadastrada com o id " + id));

        if (vacina.getPaciente().getId() != paciente.getId()) {
            throw new ConsultaNaoPertenceAoPacienteException("A vacina de id " + id + " não pertence ao paciente " + paciente.getFirstName() + " " + paciente.getLastName());
        }

        return new VacinaResponseDTO(vacina);
    }

}

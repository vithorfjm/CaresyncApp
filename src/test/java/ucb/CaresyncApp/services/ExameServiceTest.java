package ucb.CaresyncApp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import ucb.CaresyncApp.DTOs.ExameResponseDTO;
import ucb.CaresyncApp.entities.Consulta;
import ucb.CaresyncApp.entities.Exame;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.exceptions.custom.ConsultaNaoEncontradaException;
import ucb.CaresyncApp.exceptions.custom.ConsultaNaoPertenceAoPacienteException;
import ucb.CaresyncApp.repositories.ExameRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ExameServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private ExameRepository exameRepository;

    @InjectMocks
    private ExameService exameService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve retornar uma lista contendo todos os exames do usuário quando ele tiver pelo menos 1 exame cadastrado")
    void listarExamesDoUsuarioCase1() {
        User pacienteMock1 = criarUsuario(1L, "Vithor", "Félix", false);
        User pacienteMock2 = criarUsuario(2L, "Eduardo", "Silva", false);

        Exame exame1 = criarExame(1L, "Raio-x", pacienteMock1);  // Exame do paciente 1
        Exame exame2 = criarExame(2L, "Hemograma", pacienteMock1); // Exame do paciente 1
        Exame exame3 = criarExame(3L, "Sangue", pacienteMock2);   // Exame do paciente 2

        List<Exame> examesMock = List.of(exame1, exame2, exame3);

        when(exameRepository.findByPacienteId(1L)).thenReturn(List.of(exame1, exame2));  // Exames para paciente 1

        List<ExameResponseDTO> examesDoPaciente1 = exameService.listarExamesDoUsuario(pacienteMock1);

        assertEquals(2, examesDoPaciente1.size());
        assertTrue(examesDoPaciente1.stream().anyMatch(exameDTO -> exameDTO.nomeExame().equals("Raio-x")));
        assertTrue(examesDoPaciente1.stream().anyMatch(exameDTO -> exameDTO.nomeExame().equals("Hemograma")));
        assertFalse(examesDoPaciente1.stream().anyMatch(exameDTO -> exameDTO.nomeExame().equals("Sangue")));
    }

    @Test
    @DisplayName("Deve retornar uma lista de exames vazia quando ele não tiver nenhum exame cadastrado")
    void listarExamesDoUsuarioCase2() {
        User pacienteMock1 = criarUsuario(1L, "Vithor", "Félix", false);
        User pacienteMock2 = criarUsuario(2L, "Eduardo", "Silva", false);

        Exame exame1 = criarExame(1L, "Raio-x", pacienteMock1);  // Exame do paciente 1
        Exame exame2 = criarExame(2L, "Hemograma", pacienteMock1); // Exame do paciente 1
        Exame exame3 = criarExame(3L, "Sangue", pacienteMock2);   // Exame do paciente 2

        List<Exame> examesMock = List.of(exame1, exame2, exame3);

        when(exameRepository.findByPacienteId(2L)).thenReturn(List.of());  // Exames para paciente 2

        List<ExameResponseDTO> examesDoPaciente2 = exameService.listarExamesDoUsuario(pacienteMock2);

        assertTrue(examesDoPaciente2.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar todos os dados do exame com id específico caso ele exista e pertença ao usuário")
    void listarExamePeloIdCase1() {
        User pacienteMock1 = criarUsuario(1L, "Vithor", "Félix", false);

        Exame exame1 = criarExame(5L, "Raio-x", pacienteMock1);  // Exame do paciente 1

        when(exameRepository.findById(5L)).thenReturn(Optional.of(exame1));

        ExameResponseDTO exameResponse = exameService.listarExamePeloId(5L, pacienteMock1);

        assertNotNull(exameResponse);
        assertEquals("Raio-x", exameResponse.nomeExame());
        assertEquals(5L, exameResponse.id());
        assertEquals(pacienteMock1.getFirstName() + " " + pacienteMock1.getLastName(), exameResponse.nomePaciente());
    }

    @Test
    @DisplayName("Deve lançar um erro quando o exame com id passado não existe")
    void listarExamePeloIdCase2() {
        User pacienteMock1 = criarUsuario(1L, "Vithor", "Félix", false);

        Exame exame1 = criarExame(1L, "Raio-x", pacienteMock1);  // Exame do paciente 1

        when(exameRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ConsultaNaoEncontradaException.class, () -> {
            exameService.listarExamePeloId(2L, pacienteMock1);
        });
    }

    @Test
    @DisplayName("Deve lançar um erro quando o exame com id passado não pertencer ao usuário")
    void listarExamePeloIdCase3() {
        User pacienteMock1 = criarUsuario(1L, "Vithor", "Félix", false);
        User pacienteMock2 = criarUsuario(2L, "Eduardo", "Silva", false);

        Exame exame1 = criarExame(1L, "Raio-x", pacienteMock1);  // Exame do paciente 1

        when(exameRepository.findById(1L)).thenReturn(Optional.of(exame1));

        assertThrows(ConsultaNaoPertenceAoPacienteException.class, () -> {
            exameService.listarExamePeloId(1L, pacienteMock2);
        });
    }

    private User criarUsuario(Long id, String firstName, String lastName, Boolean medico) {
        User newUser = new User();
        newUser.setId(id);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setRole("patient");
        if (medico)
            newUser.setRole("doctor");
        return newUser;
    }

    private Exame criarExame(Long id, String nomeExame, User paciente) {
        var novoExame = new Exame();
        novoExame.setId(id);
        novoExame.setNomeExame(nomeExame);
        novoExame.setPaciente(paciente);
        novoExame.setData(LocalDateTime.now());
        novoExame.setMedico(criarUsuario(3L, "Jorge", "Aragao", true));
        return novoExame;
    }

}
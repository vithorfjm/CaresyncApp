package ucb.CaresyncApp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.exceptions.custom.MedicoIndisponivelException;
import ucb.CaresyncApp.repositories.ExameRepository;
import ucb.CaresyncApp.repositories.UserRepository;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar um médico aleatório da especialidade Nutrição disponível")
    void listarMedicoAleatorioPelaEspecialidadeParaConsultaCase1() {
        var medicoMock1 = criarUsuario(2L, "Pedro", "Henrique", "Nutrição");
        var medicoMock2 = criarUsuario(3L, "Ana", "Clara", "Nutrição");
        var medicosDisponiveis = List.of(medicoMock1, medicoMock2);
        LocalDateTime dataConsulta = LocalDateTime.now();
        when(userRepository.findMedicosDisponiveisParaConsultaPorEspecialidadeEData(
                "Nutrição",
                dataConsulta.minusMinutes(19),
                dataConsulta.plusMinutes(19)
        )).thenReturn(medicosDisponiveis);

        var resultado = this.userService.listarMedicoAleatorioPelaEspecialidadeParaConsulta("Nutrição", dataConsulta);

        assertNotNull(resultado);
        assertTrue(medicosDisponiveis.contains(resultado));
    }

    @Test
    @DisplayName("Deve lançar um erro quando não houver nenhum nutricionista disponível")
    void listarMedicoAleatorioPelaEspecialidadeParaConsultaCase2() {
        var listaVazia = new ArrayList<User>();
        LocalDateTime dataConsulta = LocalDateTime.now();
        when(userRepository.findMedicosDisponiveisParaConsultaPorEspecialidadeEData(
                "Nutrição",
                dataConsulta.minusMinutes(19),
                dataConsulta.plusMinutes(19)
        )).thenReturn(listaVazia);

        assertThrows(MedicoIndisponivelException.class, () -> {
            userService.listarMedicoAleatorioPelaEspecialidadeParaConsulta("Nutrição", dataConsulta);
        });
    }

    @Test
    @DisplayName("Deve retornar um médico aleatório que não tenha nenhum exame marcado para determinada data")
    void listarMedicoAleatorioPelaDataParaExameCase1() {
        LocalDateTime dataExame = LocalDateTime.now();
        var medicoMock1 = criarUsuario(2L, "Pedro", "Henrique", "Nutrição");
        var medicoMock2 = criarUsuario(3L, "Ana", "Clara", "Nutrição");
        var medicosDisponiveis = List.of(medicoMock1, medicoMock2);
        when(userRepository.findMedicosDisponiveisParaExamePorData(
                dataExame.minusMinutes(19),
                dataExame.plusMinutes(19)
        )).thenReturn(List.of(medicoMock1, medicoMock2));

        var result = userService.listarMedicoAleatorioPelaDataParaExame(dataExame);

        assertNotNull(result);
        assertTrue(medicosDisponiveis.contains(result));
    }

    @Test
    @DisplayName("Deve lançar Deve lançar um erro quando não houver nenhum médico disponível")
    void listarMedicoAleatorioPelaDataParaExameCase2() {
        LocalDateTime dataExame = LocalDateTime.now();
        var listaVazia = new ArrayList<User>();
        when(userRepository.findMedicosDisponiveisParaExamePorData(
                dataExame.minusMinutes(19),
                dataExame.plusMinutes(19)
        )).thenReturn(listaVazia);

        assertThrows(MedicoIndisponivelException.class, () -> {
            userService.listarMedicoAleatorioPelaDataParaExame(dataExame);
        });
    }

    private User criarUsuario(Long id, String firstName, String lastName, String especialidade) {
        User newUser = new User();
        newUser.setId(id);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setRole("patient");
        if (especialidade != "")  {
            newUser.setEspecialidade(especialidade);
            newUser.setRole("doctor");
        }
        return newUser;
    }

}
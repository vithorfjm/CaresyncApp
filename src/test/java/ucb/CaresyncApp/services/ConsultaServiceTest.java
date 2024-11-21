package ucb.CaresyncApp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ucb.CaresyncApp.DTOs.ConsultaResponseDTO;
import ucb.CaresyncApp.DTOs.MarcacaoConsultaDTO;
import ucb.CaresyncApp.entities.Consulta;
import ucb.CaresyncApp.entities.User;
import ucb.CaresyncApp.exceptions.custom.ConsultaNaoEncontradaException;
import ucb.CaresyncApp.exceptions.custom.ConsultaNaoPertenceAoPacienteException;
import ucb.CaresyncApp.exceptions.custom.DataForaDoLimiteException;
import ucb.CaresyncApp.repositories.ConsultaRepository;
import ucb.CaresyncApp.repositories.ExameRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ConsultaServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private ConsultaRepository consultaRepository;

    @InjectMocks
    private ConsultaService consultaService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve marcar uma consulta corretamente")
    void marcarConsultaCase1() {
        var pacienteMock = criarUsuario(1L, "Vithor", "Félix", "");
        var medicoMock = criarUsuario(2L, "Luiza", "Rodrigues","Nutrição");
        LocalDate data = LocalDate.now().plusMonths(1);
        LocalTime hora = LocalTime.of(15, 30);

        var req = new MarcacaoConsultaDTO(
                "Nutrição",
                "Rotina",
                "CSD 11",
                "UPA Taguatinga",
                "",
                data,
                hora);

        when(userService.listarMedicoAleatorioPelaEspecialidadeParaConsulta("Nutrição", data.atTime(hora))).thenReturn(medicoMock);

        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var consultaMarcada = consultaService.marcarConsulta(req, pacienteMock);

        assertNotNull(consultaMarcada);
        assertEquals("Nutrição", consultaMarcada.especialidade());
        assertEquals(data, consultaMarcada.dataConsulta());
        assertEquals(hora, consultaMarcada.hora());
    }

    @Test
    @DisplayName("Deve lançar um erro quando a data passada for superior a 3 meses a partir do dia atual")
    void marcarConsultaCase2() {
        var pacienteMock = criarUsuario(1L, "Vithor", "Félix", "");
        var data = LocalDate.now().plusMonths(4);
        var hora = LocalTime.of(10, 0);
        var req = new MarcacaoConsultaDTO(
                "Nutrição",
                "Rotina",
                "CSD 11",
                "UPA Taguatinga",
                "",
                data,
                hora);

        assertThrows(DataForaDoLimiteException.class, () -> {
            consultaService.marcarConsulta(req, pacienteMock);
        });
    }

    @Test
    @DisplayName("Deve lançar um erro quando a hora passada for antes das 09:00")
    void marcarConsultaCase3() {
        var pacienteMock = criarUsuario(1L, "Vithor", "Félix", "");
        var data = LocalDate.now().plusMonths(1);
        var hora = LocalTime.of(8, 30);
        var req = new MarcacaoConsultaDTO(
                "Nutrição",
                "Rotina",
                "CSD 11",
                "UPA Taguatinga",
                "",
                data,
                hora);

        assertThrows(DataForaDoLimiteException.class, () -> {
            consultaService.marcarConsulta(req, pacienteMock);
        });
    }

    @Test
    @DisplayName("Deve lançar um erro quando a hora passada for depois das 20:00")
    void marcarConsultaCase4() {
        var pacienteMock = criarUsuario(1L, "Vithor", "Félix", "");
        var data = LocalDate.now().plusMonths(1);
        var hora = LocalTime.of(20, 30);
        var req = new MarcacaoConsultaDTO(
                "Nutrição",
                "Rotina",
                "CSD 11",
                "UPA Taguatinga",
                "",
                data,
                hora);

        assertThrows(DataForaDoLimiteException.class, () -> {
            consultaService.marcarConsulta(req, pacienteMock);
        });
    }

    @Test
    @DisplayName("Deve retornar uma lista contendo todos as consultas do usuário quando ele tiver pelo menos 1 consulta cadastrada")
    void listarConsultasPorPacienteCase1() {
        User pacienteMock1 = criarUsuario(1L, "Vithor", "Félix", "");
        User pacienteMock2 = criarUsuario(2L, "Eduardo", "Silva", "");
        User medicoMock1 = criarUsuario(3L, "Lucas", "Neto", "Nutrição");
        User medicoMock2 = criarUsuario(4L, "Germán", "Cano", "Nutrição");

        var consulta1 = criarConsulta(1L, "Nutrição", pacienteMock1, medicoMock1, LocalDateTime.now().plusDays(12));
        var consulta2 = criarConsulta(2L, "Nutrição", pacienteMock1, medicoMock2, LocalDateTime.now().plusDays(17));
        var consulta3 = criarConsulta(3L, "Nutrição", pacienteMock2, medicoMock1, LocalDateTime.now().plusDays(11));

        when(consultaRepository.findConsultaByPacienteId(pacienteMock1.getId())).thenReturn(List.of(consulta1, consulta2));

        List<ConsultaResponseDTO> consultasDoPaciente1 = consultaService.listarConsultasPorPaciente(pacienteMock1);
        assertEquals(2, consultasDoPaciente1.size());
        assertEquals(consultasDoPaciente1.get(0).nomePaciente(), pacienteMock1.getFirstName() + " " + pacienteMock1.getLastName());
        assertEquals(consultasDoPaciente1.get(1).nomePaciente(), pacienteMock1.getFirstName() + " " + pacienteMock1.getLastName());
        assertFalse(consultasDoPaciente1.stream().anyMatch(consultaDTO -> consultaDTO.nomePaciente().startsWith("Eduardo")));
    }

    @Test
    @DisplayName("Deve retornar uma lista de consultas vazia quando ele não tiver nenhuma consulta cadastrada")
    void listarConsultasPorPacienteCase2() {
        User pacienteMock1 = criarUsuario(1L, "Vithor", "Félix", "");
        User pacienteMock2 = criarUsuario(2L, "Eduardo", "Silva", "");
        User medicoMock1 = criarUsuario(1L, "Lucas", "Neto", "Nutrição");

        var consulta1 = criarConsulta(1L, "Nutrição", pacienteMock1, medicoMock1, LocalDateTime.now().plusDays(12));
        var consulta2 = criarConsulta(2L, "Nutrição", pacienteMock1, medicoMock1, LocalDateTime.now().plusDays(17));

        when(consultaRepository.findConsultaByPacienteId(pacienteMock2.getId())).thenReturn(new ArrayList<>());

        List<ConsultaResponseDTO> consultasDoPaciente2 = consultaService.listarConsultasPorPaciente(pacienteMock2);
        assertTrue(consultasDoPaciente2.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar todos os dados da consulta com id específico caso ela exista e pertença ao usuário")
    void listarConsultaPeloIdCase1() {
        User pacienteMock = criarUsuario(1L, "Vithor", "Félix", "");
        User medicoMock = criarUsuario(1L, "Eduardo", "Silva", "Nutrição");

        var consulta = criarConsulta(5L, "Nutrição", pacienteMock, medicoMock, LocalDateTime.now());

        when(consultaRepository.findById(5L)).thenReturn(Optional.of(consulta));

        var consultaResponse = consultaService.listarConsultaPeloId(5L, pacienteMock);

        assertNotNull(consultaResponse);
        assertEquals("Nutrição", consultaResponse.especialidade());
        assertEquals(5L, consultaResponse.id());
        assertEquals(pacienteMock.getFirstName() + " " + pacienteMock.getLastName(), consultaResponse.nomePaciente());
    }

    @Test
    @DisplayName("Deve lançar um erro quando a consulta com id passado não existe")
    void listarConsultaPeloIdCase2() {
        User pacienteMock = criarUsuario(1L, "Vithor", "Félix", "");
        User medicoMock = criarUsuario(3L, "Eduardo", "Silva", "Nutrição");
        var consulta = criarConsulta(5L, "Nutrição", pacienteMock, medicoMock, LocalDateTime.now());

        when(consultaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ConsultaNaoEncontradaException.class, () -> {
            consultaService.listarConsultaPeloId(1L, pacienteMock);
        });
    }

    @Test
    @DisplayName("Deve lançar um erro quando a consulta com id passado não pertencer ao usuário")
    void listarConsultaPeloIdCase3() {
        User pacienteMock1 = criarUsuario(1L, "Vithor", "Félix", "");
        User pacienteMock2 = criarUsuario(2L, "Lucas", "Carlos", "");
        User medicoMock = criarUsuario(3L, "Eduardo", "Silva", "Nutrição");

        var consulta = criarConsulta(1L, "Nutrição", pacienteMock2, medicoMock, LocalDateTime.now());

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));

        assertThrows(ConsultaNaoPertenceAoPacienteException.class, () -> {
            consultaService.listarConsultaPeloId(1L, pacienteMock1);
        });
    }

    private User criarUsuario(Long id, String firstName, String lastName, String especialidade) {
        var newUser = new User();
        newUser.setId(id);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setRole("patient");
        if (especialidade != "") {
            newUser.setRole("doctor");
            newUser.setEspecialidade(especialidade);
        }
        return newUser;
    }

    private Consulta criarConsulta(Long id, String especialidade, User paciente, User medico, LocalDateTime data) {
        var newConsulta = new Consulta();
        newConsulta.setId(id);
        newConsulta.setEspecialidade(especialidade);
        newConsulta.setPaciente(paciente);
        newConsulta.setDataConsulta(data);
        newConsulta.setMedico(medico);
        return newConsulta;
    }
}
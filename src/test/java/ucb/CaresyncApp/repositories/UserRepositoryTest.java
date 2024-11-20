package ucb.CaresyncApp.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import ucb.CaresyncApp.DTOs.CadastroRequestDTO;
import ucb.CaresyncApp.entities.Consulta;
import ucb.CaresyncApp.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConsultaRepository consultaRepository;

    @Autowired
    EntityManager entityManager;

    List<User> pacientesList;
    List<User> medicosList;

    @BeforeEach
    void setup() {
        pacientesList = gerarPacientes();
        medicosList = gerarMedicos();
    }

    @Test
    @DisplayName("Deve retornar do banco de dados, uma lista contendo todos os médico da especialidade especifica que não tenham uma consulta marcada em até 20 minutos antes ou depois da hora passada")
    void findMedicosDisponiveisPorEspecialidadeEDataSuccess() {

        criarConsulta(pacientesList.get(0), medicosList.get(0), LocalDateTime.of(2024, 12, 10, 12, 00)); // Paciente 1, medico 0, 12:00 - 10/12/2024

        LocalDateTime data = LocalDateTime.of(2024, 12, 10, 12, 10);

        List<User> medicosDisponiveis = this.userRepository.findMedicosDisponiveisParaConsultaPorEspecialidadeEData("Pediatria", data.minusMinutes(20), data.plusMinutes(20));

        assertFalse(medicosDisponiveis.isEmpty());
        assertEquals(2, medicosDisponiveis.size());
        assertNotSame(medicosDisponiveis.get(0).getId(), medicosList.get(0).getId()); // garante que o id do 1º user da lista nao seja o médico com consulta marcada (id 0)
        assertNotSame(medicosDisponiveis.get(1).getId(), medicosList.get(0).getId()); // garante que o id do 2º user da lista nao seja o médico com consulta marcada (id 0)

    }

    @Test
    @DisplayName("Deve retornar do banco de dados, uma lista vazia de médicos quando todos os médicos estiverem ocupados com consultas na data específica")
    void findMedicosDisponiveisPorEspecialidadeEDataFail() {

        criarConsulta(pacientesList.get(1), medicosList.get(1), LocalDateTime.of(2024, 12, 10, 12, 00)); // Paciente 1, medico 1, 12:00 - 10/12/2024
        criarConsulta(pacientesList.get(3), medicosList.get(0), LocalDateTime.of(2024, 12, 10, 12, 10)); // Paciente 3, medico 0, 12:00 - 10/12/2024
        criarConsulta(pacientesList.get(2), medicosList.get(2), LocalDateTime.of(2024, 12, 10, 12, 00)); // Paciente 2, medico 2, 12:00 - 10/12/2024

        LocalDateTime data = LocalDateTime.of(2024, 12, 10, 12, 00);
        LocalDateTime vinteMinAntes = data.minusMinutes(20);
        LocalDateTime vinteMinDepois = data.plusMinutes(20);

        List<User> medicosDisponiveis = this.userRepository.findMedicosDisponiveisParaConsultaPorEspecialidadeEData("Pediatria", vinteMinAntes, vinteMinDepois);

        assertTrue(medicosDisponiveis.isEmpty());
    }


    private List<User> gerarPacientes() {
        User paciente1 = this.criarUsuario("Luciana", null);
        User paciente2 = this.criarUsuario("Ana", null);
        User paciente3 = this.criarUsuario("Jonas", null);
        User paciente4 = this.criarUsuario("Maria", null);
        return List.of(paciente1, paciente2, paciente3, paciente4);
    }

    private List<User> gerarMedicos() {
        User pediatra1 = this.criarUsuario("Carlo", "Pediatria");
        User pediatra2 = this.criarUsuario("Felipe","Pediatria");
        User pediatra3 = this.criarUsuario("Patricia","Pediatria");
        return List.of(pediatra1, pediatra2, pediatra3);
    }

    private User criarUsuario(String firstName, String especialidade){
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setRole("patient");
        if (especialidade != null) {
            newUser.setRole("doctor");
            newUser.setEspecialidade(especialidade);
        }
        this.entityManager.persist(newUser);
        return newUser;
    }


    private void criarConsulta(User paciente, User medico, LocalDateTime data) {
        Consulta novaConsulta = new Consulta();
        novaConsulta.setMedico(medico);
        novaConsulta.setPaciente(paciente);
        novaConsulta.setDataConsulta(data);
        this.entityManager.persist(novaConsulta);
    }
}
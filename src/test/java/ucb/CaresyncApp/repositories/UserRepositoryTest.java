package ucb.CaresyncApp.repositories;

import jakarta.persistence.EntityManager;
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

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Deve retornar do banco de dados, uma lista contendo todos os médico da especialidade especifica que não tenham uma consulta marcada em até 20 minutos antes ou depois da hora passada")
    void findMedicosDisponiveisPorEspecialidadeEDataSuccess() {
        List<User> pacientesList = gerarPacientes();
        List<User> medicosList = gerarMedicos();
        entityManager.flush();
        entityManager.clear();

        var consulta1 = criarConsulta(pacientesList.get(0), medicosList.get(0), LocalDateTime.of(2024, 12, 10, 12, 00)); // Paciente 1, medico 0, 12:00 - 10/12/2024

        LocalDateTime data = LocalDateTime.of(2024, 12, 10, 12, 10);

        List<User> result = this.userRepository.findMedicosDisponiveisParaConsultaPorEspecialidadeEData("Pediatria", data.minusMinutes(20), data.plusMinutes(20));

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertNotSame(result.get(0).getId(), medicosList.get(0).getId()); // garante que o id do 1º user do result nao seja o médico com consulta marcada (id 0)
        assertNotSame(result.get(1).getId(), medicosList.get(0).getId()); // garante que o id do 2º user do result nao seja o médico com consulta marcada (id 0)

    }

    @Test
    @DisplayName("Deve retornar do banco de dados, uma lista vazia de médicos quando todos os médicos estiverem ocupados com consultas na data específica")
    void findMedicosDisponiveisPorEspecialidadeEDataFail() {
        List<User> pacientesList = gerarPacientes();
        List<User> medicosList = gerarMedicos();

        var consulta1 = criarConsulta(pacientesList.get(1), medicosList.get(1), LocalDateTime.of(2024, 12, 10, 12, 00)); // Paciente 1, medico 1, 12:00 - 10/12/2024
        var consulta2 = criarConsulta(pacientesList.get(3), medicosList.get(0), LocalDateTime.of(2024, 12, 10, 12, 10)); // Paciente 3, medico 0, 12:00 - 10/12/2024
        var consulta3 = criarConsulta(pacientesList.get(2), medicosList.get(2), LocalDateTime.of(2024, 12, 10, 12, 00)); // Paciente 2, medico 2, 12:00 - 10/12/2024
        entityManager.flush();
        entityManager.clear();

        LocalDateTime data = LocalDateTime.of(2024, 12, 10, 12, 00);
        LocalDateTime vinteMinAntes = data.minusMinutes(20);
        LocalDateTime vinteMinDepois = data.plusMinutes(20);

        List<User> result = this.userRepository.findMedicosDisponiveisParaConsultaPorEspecialidadeEData("Pediatria", vinteMinAntes, vinteMinDepois);

        result.forEach(medico -> System.out.println("Médico: " + medico.getFirstName()));

        assertTrue(result.isEmpty());
    }


    private List<User> gerarPacientes() {
        var paciente1DTO = new CadastroRequestDTO("Luciana", "Pereira", "Feminino", LocalDate.of(1993, 11, 5), "654.321.987-00", "Quadra 45, Conjunto C", "23456-789", "Gama", "DF", "(61) 99567-8901", "luciana.pereira@gmail.com", "321", "712345678912349");
        var paciente2DTO = new CadastroRequestDTO("Ana", "Silva", "Feminino", LocalDate.of(1995, 5, 10), "987.654.321-00", "QNP 10", "67890-456", "Taguatinga", "DF", "(61) 99234-5678", "ana.silva@gmail.com", "123", "712345678912346");
        var paciente3DTO = new CadastroRequestDTO("Carlos", "Oliveira", "Masculino", LocalDate.of(1988, 8, 22), "321.654.987-55", "QNP 20", "34567-890", "Plano Piloto", "DF", "(61) 99345-6789", "carlos.oliveira@gmail.com", "123", "712345678912347");
        var paciente4DTO = new CadastroRequestDTO("Maria", "Fernandes", "Feminino", LocalDate.of(1992, 2, 28), "111.222.333-44", "QNP 30", "98765-432", "Samambaia", "DF", "(61) 99456-7890", "maria.fernandes@gmail.com", "123", "712345678912348");
        User paciente1 = this.criarUsuario(paciente1DTO, null);
        User paciente2 = this.criarUsuario(paciente2DTO, null);
        User paciente3 = this.criarUsuario(paciente3DTO, null);
        User paciente4 = this.criarUsuario(paciente4DTO, null);
        return List.of(paciente1, paciente2, paciente3, paciente4);
    }

    private List<User> gerarMedicos() {
        var pediatra1DTO = new CadastroRequestDTO("Carlo", "Anccelotti","Masculino", LocalDate.of(1980, 10, 20), "123.123.123-12","QNP 15","12345-123","Ceilândia","DF","(61) 99156-1244","carlin@gmail.com","123","712345678912345");
        var pediatra2DTO = new CadastroRequestDTO("Felipe", "Costa", "Masculino", LocalDate.of(1987, 3, 15), "555.444.333-22", "Rua do Sol, 80", "45678-123", "Águas Claras", "DF", "(61) 99678-1234", "felipe.costa@gmail.com", "123", "712345678912350");
        var pediatra3DTO = new CadastroRequestDTO("Patrícia", "Lima", "Feminino", LocalDate.of(2000, 6, 28), "444.333.222-11", "Avenida Brasília, 1200", "56789-234", "Sobradinho", "DF", "(61) 99789-2345", "patricia.lima@gmail.com", "123", "712345678912351");

        User pediatra1 = this.criarUsuario(pediatra1DTO, "Pediatria");
        User pediatra2 = this.criarUsuario(pediatra2DTO, "Pediatria");
        User pediatra3 = this.criarUsuario(pediatra3DTO, "Pediatria");
        return List.of(pediatra1, pediatra2, pediatra3);

    }

    private User criarUsuario(CadastroRequestDTO dados, String especialidade){
        User newUser = new User(dados, passwordEncoder);
        if (especialidade != null) {
            newUser.setRole("doctor");
            newUser.setEspecialidade(especialidade);
        }
        this.entityManager.persist(newUser);
        return newUser;
    }


    private Consulta criarConsulta(User paciente, User medico, LocalDateTime data) {
        Consulta novaConsulta = new Consulta();
        novaConsulta.setMedico(medico);
        novaConsulta.setPaciente(paciente);
        novaConsulta.setDataConsulta(data);
        novaConsulta.setStatus("Agendada");
        novaConsulta.setEspecialidade("Nutrição");
        novaConsulta.setTipo("Consulta");
        novaConsulta.setLocal("Clínica XYZ");
        novaConsulta.setEndereco("Rua ABC, 123");
        novaConsulta.setObservacoes("Nenhuma observação");
        this.entityManager.persist(novaConsulta);
        return novaConsulta;
    }

    @TestConfiguration
    static class PasswordEncoderTestConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}
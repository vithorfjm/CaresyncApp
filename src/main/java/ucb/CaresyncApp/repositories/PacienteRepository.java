package ucb.CaresyncApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucb.CaresyncApp.entities.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}

package ucb.CaresyncApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucb.CaresyncApp.entities.Exame;

import java.util.List;

public interface ExameRepository extends JpaRepository<Exame, Long> {
    List<Exame> findByPacienteId(Long pacienteId);
}

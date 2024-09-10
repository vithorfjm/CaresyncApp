package ucb.CaresyncApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucb.CaresyncApp.entities.Profissional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
}

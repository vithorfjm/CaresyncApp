package ucb.CaresyncApp.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacinas")
public class Vacina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private LocalDateTime dataAplicacao;
    private LocalDate dataRetorno;
    @Column(name = "localAplicacao")
    private String localDeAplicacao;
    @Column(name = "statusVacina")
    private String status;
    @ManyToOne
    @JoinColumn(name = "paciente_id", referencedColumnName = "id")
    private User paciente;
    @ManyToOne
    @JoinColumn(name = "medico_id", referencedColumnName = "id")
    private User medico;

    public Vacina() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDateTime dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public LocalDate getDataRetorno() {
        return dataRetorno;
    }

    public void setDataRetorno(LocalDate dataRetorno) {
        this.dataRetorno = dataRetorno;
    }

    public String getLocalDeAplicacao() {
        return localDeAplicacao;
    }

    public void setLocalDeAplicacao(String localDeAplicacao) {
        this.localDeAplicacao = localDeAplicacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getPaciente() {
        return paciente;
    }

    public void setPaciente(User paciente) {
        this.paciente = paciente;
    }

    public User getMedico() {
        return medico;
    }

    public void setMedico(User medico) {
        this.medico = medico;
    }
}

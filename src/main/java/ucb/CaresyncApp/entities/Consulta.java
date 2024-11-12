package ucb.CaresyncApp.entities;

import jakarta.persistence.*;
import ucb.CaresyncApp.DTOs.MarcacaoConsultaDTO;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", referencedColumnName = "id")
    private User paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id", referencedColumnName = "id")
    private User medico;

    private String status;

    private String especialidade;

    private String tipo;

    private String local;

    private String endereco;

    private String observacoes;

    @Column(name="data")
    private LocalDateTime dataConsulta;

    public Consulta() {
    }

    public Consulta(MarcacaoConsultaDTO dto, User paciente, User medico) {
        this.dataConsulta = dto.dataConsulta().atTime(dto.hora());
        this.paciente = paciente;
        this.medico = medico;
        this.status = "Agendada";
        this.especialidade = dto.especialidade();
        this.local = dto.local();
        this.endereco = dto.endereco();
        this.tipo = dto.tipo();
        this.observacoes = dto.observacoes();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}

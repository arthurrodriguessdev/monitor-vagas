package arthurrodrigues.monitorvagas.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "preferencia")
@AllArgsConstructor
@NoArgsConstructor
public class Preferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long Id;

    @ManyToOne()
    @JoinColumn(name = "id_usuario")
    public PreferenciaUsuario preferenciaUsuario;
    public String nome;

    public Preferencia(PreferenciaUsuario preferenciaUsuario, String nome){
        this.preferenciaUsuario = preferenciaUsuario;
        this.nome = nome;
    }
}
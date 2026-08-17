package arthurrodrigues.monitorvagas.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "preferencia_usuario")
public class PreferenciaUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long Id;

    @Column(length = 255, unique = true, nullable = false)
    public String email;

    @OneToMany(mappedBy = "preferenciaUsuario")
    public List<Preferencia> preferencias;

    public PreferenciaUsuario(String email){
        this.email = email;
    }
}

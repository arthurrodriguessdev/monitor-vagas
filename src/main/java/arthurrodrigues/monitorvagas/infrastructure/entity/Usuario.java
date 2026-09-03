package arthurrodrigues.monitorvagas.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entidade de autenticação
 * */
@Entity
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 255, nullable = false)
    public String nome;

    @Column(length = 255, nullable = false, unique = true)
    public String email;

    @Column(nullable = false)
    public String senha;

    @OneToMany(mappedBy = "usuario")
    public List<Preferencia> preferencias;

    @OneToMany(mappedBy = "usuario")
    public List<VagaRecebida> vagasRecebidas;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
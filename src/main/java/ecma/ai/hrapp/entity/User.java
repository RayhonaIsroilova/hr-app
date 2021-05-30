package ecma.ai.hrapp.entity;

import ecma.ai.hrapp.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Collection;
import java.util.Set;

@Entity(name = "users")
@EqualsAndHashCode(callSuper = true)
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbsEntity implements UserDetails {


    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Email
    @Column(unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles; //Manager,Director

    private String verifyCode;

    private String position;//HR MANAGER,

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = false;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public User(String username, String password, @Email String email, Set<Role> roles, String position, boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.position = position;
        this.enabled = enabled;
    }

}

package ru.kata.spring.boot_security.demo.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor

@Entity
@Table(name = "users")
public class User implements UserDetails {
    // region Поля
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "birth_year")
    private Integer birthYear;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "is_account_non_expired")
    private boolean accountNonExpired;
    @Column(nullable = false, name = "is_account_non_locked")
    private boolean accountNonLocked;
    @Column(nullable = false, name = "is_credentials_non_expired")
    private boolean credentialsNonExpired;
    @Column(nullable = false, name = "is_enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    // endregion

// Этот конструктор в логике программы не используется
//    public User(String username, String password, boolean enabled, boolean accountNonExpired,
//                boolean credentialsNonExpired, boolean accountNonLocked,
//                String firstName, String secondName, Integer birthYear) {
//        if (((username == null) || "".equals(username)) || (password == null)) {
//            throw new IllegalArgumentException(
//                    "Cannot pass null or empty values to constructor");
//        }
//
//        this.username = username;
//        this.password = password;
//        this.enabled = enabled;
//        this.accountNonExpired = accountNonExpired;
//        this.credentialsNonExpired = credentialsNonExpired;
//        this.accountNonLocked = accountNonLocked;
//
//        this.firstName = firstName;
//        this.secondName = secondName;
//        this.birthYear = birthYear;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Преобразуем Set<Role> в Set<GrantedAuthority>
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }
    public void addRole(Role role) {
        if (roles == null) roles = new HashSet<>();
        roles.add(role);
    }

    public void removeRole(Role role) {
        if (roles != null) roles.remove(role);
    }
}


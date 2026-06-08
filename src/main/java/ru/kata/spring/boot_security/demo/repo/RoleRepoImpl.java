package ru.kata.spring.boot_security.demo.repo;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepoImpl implements RoleRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> findAll() {
        String jpql = "select r from Role r";
        return entityManager.createQuery(jpql, Role.class).getResultList();
    }

    @Override
    public void save(Role role) {
        if (role.getId() == null) {
            entityManager.persist(role);
        } else {
            entityManager.merge(role);
        }
    }

    @Override
    public void deleteById(Long id) {
        Role role = entityManager.find(Role.class, id);
        if (role != null) {
            entityManager.remove(role);
        }
    }

    @Override
    public Optional<Role> findById(Long id) {
        Role role = entityManager.find(Role.class, id);
        return Optional.ofNullable(role);
    }
}

package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repo.RoleRepository;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepo;
    public RoleServiceImpl(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    @Transactional
    public Role save(Role role) {
        return roleRepo.save(role);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        roleRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Role findById(Long id) {
        return roleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }
}
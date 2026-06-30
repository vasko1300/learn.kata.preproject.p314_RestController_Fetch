package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.domain.entity.Role;
import ru.kata.spring.boot_security.demo.domain.dto.RoleDto;
import ru.kata.spring.boot_security.demo.domain.dto.RoleEditDto;
import ru.kata.spring.boot_security.demo.domain.mapper.RoleMapper;
import ru.kata.spring.boot_security.demo.repo.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepo;
    private final RoleMapper roleMapper;
    private final UserDetailsServiceImpl userDetailsService;
    public RoleServiceImpl(RoleRepository roleRepo, RoleMapper roleMapper, UserDetailsServiceImpl userDetailsService) {
        this.roleRepo = roleRepo;
        this.roleMapper = roleMapper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    @Transactional
    public RoleDto save(RoleDto roleDto) {
        return roleMapper.toDto(roleRepo.save(roleMapper.toEntity(roleDto)));
    }

    @Transactional
    @Override
    public RoleDto edit(Long id, RoleEditDto roleEditDto) {
        Role existingRole = findById(id);
        roleEditDto.name().ifPresent(existingRole::setName);
        return roleMapper.toDto(existingRole);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        roleRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> findAll() {
        return roleRepo.findAll().stream().map(roleMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto findRoleDtoById(Long id) {
        return roleMapper.toDto(
                roleRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Role not found"))
        );
    }

    @Transactional(readOnly = true)
    @Override
    public RoleDto findRoleDtoByName(String name) {
        return roleMapper.toDto(
                roleRepo.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Role not found"))
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Role findById(Long id) {
        return roleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public Role findByName(String name) {
        String roleName = name.startsWith("ROLE_") ? name : "ROLE_" + name;
        return roleRepo.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> principalRoleNames(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toList());
    }
}
package ru.kata.spring.boot_security.demo.service;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.dao.UserDao;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserDao userDao;

    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("=== loadUserByUsername вызван для username: {} ===", username);
        User user = null;
        try {
            user = userDao.findByUsernameWithRoles(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
        } catch (UsernameNotFoundException e) {
            log.info("=== {} ===", e.getMessage());
            throw e;
        }
//        Hibernate.initialize(user.getRoles());
        return user;
    }
}

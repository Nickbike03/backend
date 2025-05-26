package com.unical.webapplication.back.service;

import java.sql.SQLException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.unical.webapplication.back.model.UserRole;
import com.unical.webapplication.back.model.Utente;
import com.unical.webapplication.back.persistence.implementation.UtenteDaoImpl;

@Service
public class UtenteService implements UserDetailsService {

    private final UtenteDaoImpl utenteDao;
    private final PasswordEncoder passwordEncoder;

    public UtenteService(UtenteDaoImpl utenteDao, PasswordEncoder passwordEncoder) {
        this.utenteDao = utenteDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Utente utente = utenteDao.findByEmail(email);
            if(utente == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return utente;
        } catch (SQLException e) {
            throw new UsernameNotFoundException("Database error", e);
        }
    }

    public boolean registerUser(Utente utente) {
        try {
            if(utenteDao.findByEmail(utente.getEmail()) != null) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        utente.setRole(UserRole.ROLE_USER);
        return utenteDao.saveUtente(utente);
    }
}

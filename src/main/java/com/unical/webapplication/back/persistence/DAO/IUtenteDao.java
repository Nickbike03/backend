package com.unical.webapplication.back.persistence.DAO;

import java.sql.SQLException;

import com.unical.webapplication.back.model.Utente;

public interface IUtenteDao {
    
    public Utente findByEmail(String email) throws  SQLException;

    public boolean saveUtente(Utente utente) throws SQLException;
}

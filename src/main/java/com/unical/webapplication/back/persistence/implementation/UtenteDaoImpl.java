package com.unical.webapplication.back.persistence.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.unical.webapplication.back.model.UserRole;
import com.unical.webapplication.back.model.Utente;
import com.unical.webapplication.back.persistence.DAO.IUtenteDao;
import com.unical.webapplication.back.persistence.DBManager;

@Repository
public class UtenteDaoImpl implements IUtenteDao {


    
    @Override
    public Utente findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM utente WHERE email = ?";
        
        try(Connection conn = DBManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql,
         PreparedStatement.RETURN_GENERATED_KEYS))
                 
        {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUtenteFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public boolean saveUtente(Utente utente) {
        String sql = "INSERT INTO utente (name, surname, email, password, faculty, role) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBManager.getConnection();PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, utente.getName());
            pstmt.setString(2, utente.getSurname());
            pstmt.setString(3, utente.getEmail());
            pstmt.setString(4, utente.getPassword());
            pstmt.setString(5, utente.getFaculty());
            pstmt.setString(6, utente.getRole().toString());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Utente mapUtenteFromResultSet(ResultSet rs) throws SQLException {
        Utente utente = new Utente();
        utente.setId(rs.getInt("id"));
        utente.setName(rs.getString("name"));
        utente.setSurname(rs.getString("surname"));
        utente.setEmail(rs.getString("email"));
        utente.setPassword(rs.getString("password"));
        utente.setFaculty(rs.getString("faculty"));
        utente.setRole(UserRole.valueOf(rs.getString("role")));
        return utente;
    }
}

package interfaces;

import java.sql.SQLException;
import java.util.List;

public interface UService<T> {
    public void addUtilisateur(T t) throws SQLException;
    void deleteUtilisateur(int id) throws SQLException;

    public void updateUtilisateur(T t) throws SQLException;
    public List<T> getData() throws SQLException;
}


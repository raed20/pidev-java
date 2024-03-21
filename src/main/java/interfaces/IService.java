package interfaces;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    public void addPerson(T t) throws SQLException;
    void deletePerson(int id) throws SQLException;

    public void updatePerson(T t) throws SQLException;
    public List<T> getData() throws SQLException;
}


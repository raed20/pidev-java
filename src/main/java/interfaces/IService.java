package interfaces;

import entities.Personnes;

import java.sql.SQLException;
import java.util.List;

public interface IService <T>{
    void addPerson(Personnes personne) throws SQLException;

    void deletePerson(int id) throws SQLException;

    void updatePerson(Personnes personne) throws SQLException;

    List<Personnes> getData() throws SQLException;

    //CRUD
    void add(T t);
    void update(T t);
    void delete(int id);
    List<T> getAll();
    T getOne(int id);
}

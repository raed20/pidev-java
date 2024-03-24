package interfaces;

import java.util.List;

public interface IService <T>{
    //CRUD
    void add(T t);
    void update(T t);
    void delete(int id);
    List<T> getAll();
    T getOne(int id);
}

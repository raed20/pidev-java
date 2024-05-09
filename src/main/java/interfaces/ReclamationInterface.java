package interfaces;

import java.util.List;

public interface ReclamationInterface<T> {
    boolean ajouter(T t);
    boolean supprimer(T t);
    boolean modifier(T t);
    public List<T> afficher();
    T afficher_id(int id);
}

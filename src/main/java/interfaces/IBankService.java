package interfaces;

import java.util.List;
import java.util.Optional;

public interface IBankService<T> {
    /**
     * Enregistre une entité dans la base de données.
     * @param entity L'entité à enregistrer.
     * @return L'entité enregistrée.
     */
    T save(T entity);

    /**
     * Récupère une entité par son identifiant.
     * @param id L'identifiant de l'entité.
     * @return Une entité, si trouvée.
     */
    Optional<T> findById(int id);

    /**
     * Récupère toutes les entités.
     * @return Une liste de toutes les entités.
     */
    List<T> findAll();

    /**
     * Met à jour une entité dans la base de données.
     * @param entity L'entité à mettre à jour.
     * @return L'entité mise à jour.
     */
    T update(T entity);

    /**
     * Supprime une entité par son identifiant.
     * @param id L'identifiant de l'entité à supprimer.
     */
    void deleteById(int id);
}

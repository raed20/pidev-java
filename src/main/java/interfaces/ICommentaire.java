package interfaces;

import entities.Commentaire;

import java.util.List;

public interface ICommentaire {
    void addCommentaire(Commentaire commentaire);

    void deleteCommentaire(int id);

    void updateCommentaire(Commentaire commentaire);

    List<Commentaire> getAllCommentaire();
}

package interfaces;

import entities.Opportunite;

import java.util.List;

public interface IOpportunite {
    void addOpportunite(Opportunite opportunite);

    void deleteOpportunite(int id);

    void updateOpportunite(Opportunite opportunite);

    List<Opportunite> getAllOpportunites();
}

package interfaces;

import entities.Investissement;

import java.util.List;

public interface IInvestissement {
    void addInvestissement(Investissement investissement);

    void deleteInvestissement(int id);

    void updateInvestissement(Investissement investissement);

    List<Investissement> getAllInvestissements();
    List<Investissement> getOpportuniteByInvestissementId(int opportuniteId);

}

package interfaces;

import entities.Pret;

import java.util.List;

public interface ILoanService<T> {
    public String addLoan(T t) throws Exception;
    public void deleteLoan(int id) throws Exception;
    public String updateLoan(int id, T t) throws Exception;
    public List<T> getDataLoan() throws Exception;
}

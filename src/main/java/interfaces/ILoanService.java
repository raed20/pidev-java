package interfaces;

import entities.Pret;

import java.util.List;

public interface ILoanService<T> {
    public String addLoan(T t,int bankId) throws Exception;
    public void deleteLoan(int id) throws Exception;
    public String updateLoan(T t,int bankId) throws Exception;
    public List<T> getDataLoan() throws Exception;
}
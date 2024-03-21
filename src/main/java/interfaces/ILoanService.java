package interfaces;

import java.util.List;

public interface ILoanService<T> {
    public void addLoan(T t) throws Exception;
    public void deleteLoan(int id) throws Exception;
    public void updateLoan(int id) throws Exception;
    /*public List<T> getDataLoan(T t);*/
}

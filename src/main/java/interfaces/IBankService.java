package interfaces;

import java.util.List;

public interface IBankService<T> {

    public void addBank(T t) throws Exception;
    public void deleteBank(int id) throws Exception;
    public void updateBank(int id) throws Exception;
    public List<T> getDataBank() throws Exception;
}

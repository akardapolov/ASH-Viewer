package store.dao.database;

public interface IParameterStringDAO {
    int getCheckOrLoadParameter(String parameter);
    String getParameterStrById(int id);
}

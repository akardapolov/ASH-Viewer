package store.dao.database;

public interface IParameterDoubleDAO {
    int getCheckOrLoadParameter(double parameter);
    double getParameterStrById(int id);
}

package hometask_jdbc.db;

import java.sql.SQLException;
import java.util.List;

public interface AbstractDao <T,K>{

    void save (T t) throws SQLException;

    T getById (K k);

    List<T> getAll ();

    void deleteById(K k);

    void update (T t);

    void close();
}

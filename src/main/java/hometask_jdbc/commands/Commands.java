package hometask_jdbc.commands;

import java.sql.SQLException;

public interface Commands {
    void desc();

    void add () ;

    void getByID();

    void update();

    void showAll();

    void deleteByID ();



}

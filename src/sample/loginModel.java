package sample;

import java.sql.Connection;
import java.sql.SQLException;
import dbUtil.dbConnection;

public class loginModel {

    Connection connection;

    public loginModel(){
        try {
            this.connection = dbConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (this.connection == null) {
            System.exit(1);
        }
    }//loginModel

    public boolean isDatabaseConnection(){
        return this.connection != null;
    }//isDatabaseConnection



}//calss

package sample;

import admin.adminController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    private loginModel loginmodel = new loginModel();

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton btnlogin;

    @FXML
    private Label loginStatus;

    @FXML
    private Label dbStatus;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (this.loginmodel.isDatabaseConnection()){
            this.dbStatus.setText("Connected to DB.");

        }else{
            this.dbStatus.setText("Not Connect to DB.");
        }
    }//initialii
    @FXML
    public  void  Login(ActionEvent event){
        try{
            if (this.loginmodel.isLogin(username.getText(),password.getText())){
                Stage stage = (Stage) this.btnlogin.getScene().getWindow();
                stage.close();
                adminDashborad();

            }else {
                loginStatus.setText("Your username or password is in");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//isLonig

    private void adminDashborad() {
        try{
            Stage adminStage = new Stage();
            FXMLLoader adminLoader = new FXMLLoader();
            Pane adminRoot = (Pane) adminLoader.load(getClass().getResource("/admin/adminDashboard.fxml").openStream());
            adminController admincontroller = adminLoader.getController();
            Scene scene = new Scene(adminRoot);
            adminStage.setScene(scene);
            adminStage.setTitle("Admin Dashborad");
            adminStage.setResizable(false);
            adminStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }//adminDashborad
}//class

package admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Main;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class adminController implements Initializable{
    private dbConnection db; //ตรวจสอบ
    private ObservableList <StudentData> data;//เก็บข้อมูล

    //sceneBuilder--view--sohw sample
    @FXML
    protected TableView<StudentData> studentTable;

    @FXML
    private TableColumn<StudentData, String> idcolum;

    @FXML
    private TableColumn<StudentData, String> fristnamecolum;

    @FXML
    private TableColumn<StudentData, String> lastnamecolum;

    @FXML
    private TableColumn<StudentData, String> emailcolum;

    @FXML
    private TableColumn<StudentData, String> dobcolum;

    @FXML
    private JFXButton btnLoad;

    @FXML
    private TextField searchTxt;


    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtFristName;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXDatePicker txtDOB;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            this.db = new dbConnection();

    }//Initializable
    @FXML
    private void loadStudentData(ActionEvent event){
        try{
            Connection conn = dbConnection.getConnection();
            this.data = FXCollections.observableArrayList();
            //sql
            String sql = "select * from student";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()){
                this.data.add(new StudentData(rs.getString(1),
                       rs.getString(2),
                       rs.getString(3),
                      rs.getString(4),
                      rs.getString(5)));

            }//while
        } catch (SQLException e){
            e.printStackTrace();
        }
//put data to tableview
        this.idcolum.setCellValueFactory(new PropertyValueFactory<StudentData,String>("id"));
        this.fristnamecolum.setCellValueFactory(new PropertyValueFactory<StudentData,String>("firstName"));
        this.lastnamecolum.setCellValueFactory(new PropertyValueFactory<StudentData,String>("lastName"));
        this.emailcolum.setCellValueFactory(new PropertyValueFactory<StudentData,String>("email"));
        this.dobcolum.setCellValueFactory(new PropertyValueFactory<StudentData,String>("DOB"));

        this.studentTable.setItems(null);
        this.studentTable.setItems(this.data);


        //Filter Data in TableView
        FilteredList<StudentData> filteredData =
                new FilteredList<>(data, e -> true);
        searchTxt.setOnKeyReleased(e -> {
            searchTxt.textProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        filteredData.setPredicate(StudentData -> {
                            if (newValue == null || newValue.isEmpty()) {
                                return true;
                            }
                            String lowerCaseFilter = newValue.toLowerCase();
                            if (StudentData.getId().contains(newValue)) {
                                return true;
                            } else if
                                    (StudentData.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            } else if
                                    (StudentData.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            return false;
                        });
                    });
            SortedList<StudentData> sortedData =
                    new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(
                    studentTable.comparatorProperty());
            studentTable.setItems(sortedData);

        });
            }//loadStudentData

    @FXML
    private  void  addStudent (ActionEvent event){
        String sqlInsert = "insert into student(ID,firstName,lastName,email,DOB) values (?,?,?,?,?)";

        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement pr = conn.prepareStatement(sqlInsert);
            pr.setString(1,this.txtID.getText());
            pr.setString(2,this.txtFristName.getText());
            pr.setString(3,this.txtLastName.getText());
            pr.setString(4,this.txtEmail.getText());
            pr.setString(5,this.txtDOB.getEditor().getText());

            pr.execute();
            pr.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadStudentData(new ActionEvent());

    }//addStudent
    @FXML
    private void clearForm (ActionEvent event){
        this.txtID.setText("");
        this.txtFristName.setText("");
        this.txtLastName.setText("");
        this.txtEmail.setText("");
        this.txtDOB.setValue(null);
    }//clearForm

    @FXML
    private void deleteStudent(ActionEvent event){
        //get data form TableView
        StudentData std = studentTable.getSelectionModel().getSelectedItem(); //เลือกTableView
        //DialogMessage คำเตือนต้องการลบหรื่อไม่
        JOptionPane.showConfirmDialog(null,"Do you want to delete student name: "
                +std.getFirstName()+" "+std.getLastName());
        //delete
        if (std != null){
            String sqlDelete = "delete from student where ID =?";
            try {
                Connection conn =dbConnection.getConnection();
                PreparedStatement pr = conn.prepareStatement(sqlDelete);
                pr.setString(1,std.getId());
                pr.executeUpdate();
                pr.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            System.exit(1);
        }
        loadStudentData(new ActionEvent());

    }//deleteStudent
@FXML
    private void editStudent (ActionEvent event){
        StudentData std = studentTable.getSelectionModel().getSelectedItem();
        if (std != null){
            txtID.setText(std.getId());
            //disable txtfiled to read only อ่านอย่างเดียวไม่ให้แก้ไข
            txtID.setDisable(true);
            txtFristName.setText(std.getFirstName());
            txtLastName.setText(std.getLastName());
            txtEmail.setText(std.getEmail());
            txtDOB.getEditor().setText(std.getDOB());
        }else {
            System.exit(1);
        }

    }//editStudent
    @FXML
    private void saveStudent(ActionEvent event){
        StudentData std = studentTable.getSelectionModel().getSelectedItem();
        //DialogMessage คำเตือนต้องการลบหรื่อไม่
        JOptionPane.showConfirmDialog(null,"Do you want to update student ID: "
                +std.getId());
        String sqlUpdate = "update student set firstName =?,"+"lastName =?,email =?,DOB =? where ID =?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement pr = conn.prepareStatement(sqlUpdate);
            pr.setString(1,this.txtFristName.getText());
            pr.setString(2,this.txtLastName.getText());
            pr.setString(3,this.txtEmail.getText());
            pr.setString(4,this.txtDOB.getEditor().getText());
            pr.setString(5,this.txtID.getText());

            pr.executeUpdate();
            pr.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadStudentData(new ActionEvent());
    }//saveStudent
@FXML
    private void logout (ActionEvent event) throws Exception {
    ((Node)event.getSource()).getScene().getWindow().hide();
    Stage primartStage = new Stage();
    Main m = new Main();
    m.start(primartStage);


}//logout
}//class

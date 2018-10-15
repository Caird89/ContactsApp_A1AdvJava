package Views;

import Models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AllContactsViewController implements Initializable {


    // configuring window and table
    @FXML private Label allContactsLabel;
    @FXML private TableView<Models.Contact> allContactsTableView;
    @FXML private TableColumn<Models.Contact, Integer> idColumn;
    @FXML private TableColumn<Models.Contact, String> firstNameColumn;
    @FXML private TableColumn<Models.Contact, String> lastNameColumn;
    @FXML private TableColumn<Models.Contact, LocalDate> birthdayColumn;
    @FXML private TableColumn<Models.Contact, String> addressColumn;
    @FXML private TableColumn<Models.Contact, String> phoneColumn;
    @FXML private TextField allContactsSearchTextField;
    @FXML private Button allContactsEditContactButton;
    @FXML private Button allContactsSearchButton;
    @FXML private Button allContactsCreateNewButton;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        //  set up the columns in the table
        idColumn.setCellValueFactory(new PropertyValueFactory<Contact, Integer>("contact_id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<Contact, LocalDate>("birthday"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("phone_number"));


//        //Update the table to allow for the first and last name fields
//        //to be editable
//        tableView.setEditable(true);
//        idColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        addressColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//
//        //This will allow the table to select multiple rows at once
//        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    /**
     * This method will change scenes to CreateContactViewController when the Create Contact
     * Button is pushed
     */
    public void allContactsCreateNewButtonPushed(ActionEvent event) throws IOException {
        Parent createContact = FXMLLoader.load(getClass().getResource("CreateContact.fxml"));
        Scene createContactScene = new Scene(createContact);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(createContactScene);
        window.show();
    }

    public void passContacts() throws SQLException {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();

        Connection dbConn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            dbConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/A1Java", "root", "root");

            statement = dbConn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Contacts");

            while (resultSet.next()) {
                Contact newContact = new Contact(
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getDate("birthday").toLocalDate(),
                        resultSet.getString("address"),
                        resultSet.getString("phone_number"),
                        newContact.setImageFile(new File(resultSet.getString("image")));
                        newContact.setContactID(resultSet.getInt("contact_id"));

                contacts.add(newContact);
            }

            allContactTableView.getItems().addAll(contacts);

        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if (dbConn != null)
                dbConn.close();
            if (statement != null)
                statement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }
}



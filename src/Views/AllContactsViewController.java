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
    @FXML private TableView<Contact> allContactsTableView;
    @FXML private TableColumn<Contact, Integer> idColumn;
    @FXML private TableColumn<Contact, String> firstNameColumn;
    @FXML private TableColumn<Contact, String> lastNameColumn;
    @FXML private TableColumn<Contact, LocalDate> birthdayColumn;
    @FXML private TableColumn<Contact, String> addressColumn;
    @FXML private TableColumn<Contact, String> phoneColumn;
    @FXML private TextField allContactsSearchTextField;
    @FXML private Button allContactsEditContactButton;
    @FXML private Button allContactsSearchButton;
    @FXML private Button allContactsCreateNewButton;


    /**
     * Initializes the controller class.
     * (this code is run first)
     */
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        //  set up the columns in the table
        idColumn.setCellValueFactory(new PropertyValueFactory<Contact, Integer>("contactID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<Contact, LocalDate>("birthday"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("phone"));

        try{
            passContacts();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
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

    /**
     * passes contacts to table view to be displayed
     * @throws SQLException
     */
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
                        resultSet.getString("phone_number"));
                        newContact.setContactID(resultSet.getInt("contact_id"));
                        newContact.setImageFile(new File(resultSet.getString("image")));

                contacts.add(newContact);
            }

            allContactsTableView.getItems().addAll(contacts);

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



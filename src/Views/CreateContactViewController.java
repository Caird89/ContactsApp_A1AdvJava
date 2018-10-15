package Views;

import Models.Contact;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateContactViewController implements Initializable {

    @FXML private TextField addressTextField;
    @FXML private Button cancelButton;
    @FXML private TextField phoneTextField;
    @FXML private TextField lastNameTextField;
    @FXML private DatePicker birthdayDatePicker;
    @FXML private Button chooseImageButton;
    @FXML private TextField firstNameTextField;
    @FXML private Button saveContactButton;
    @FXML private ImageView imageImageView;
    @FXML private Label errorMessageLabel;


    private Contact contact;
    private File contactImageFile;
    private boolean changedImageFile;

    /**
     *
     */
    public void createContactSaveButton(ActionEvent event) throws IOException {
        try {
            Contact contact = new Contact(firstNameTextField.getText(), lastNameTextField.getText(), birthdayDatePicker.getValue(),
                    addressTextField.getText(), phoneTextField.getText());

            errorMessageLabel.setText(""); // success needs no error message

            contact.addToDB();

            Parent createContact = FXMLLoader.load(getClass().getResource("AllContacts.fxml"));
            Scene createContactScene = new Scene(createContact);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(createContactScene);
            window.show();


        } catch (Exception e) {
            errorMessageLabel.setText(e.getMessage());
        }
    }


    /**
     *
     */
    public void chooseImageButtonPushed(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);


        String userFileString = System.getProperty("user.home") + "\\Pictures";
        File userFile = new File(userFileString);

        if (!userFile.canRead())
            userFile = new File(System.getProperty("user.home"));

        fileChooser.setInitialDirectory(userFile);

        File tempImgFile = fileChooser.showOpenDialog(stage);

        if (tempImgFile != null) {
            contactImageFile = tempImgFile;

            if (contactImageFile.isFile()) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(contactImageFile);
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageImageView.setImage(image);
                    changedImageFile = true;
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // setting the start values for scene variables
        errorMessageLabel.setText("");
        changedImageFile = false;

        //loads the default image for contact view controller image view
        try {
            contactImageFile = new File("./src/images/defaultPerson.png");
            BufferedImage bufferedImage = ImageIO.read(contactImageFile);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageImageView.setImage(image);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}


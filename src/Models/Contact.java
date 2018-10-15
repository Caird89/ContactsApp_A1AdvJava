package Models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;

/**
 * Defining a Contact object within our application
 */
public class Contact {
    private String firstName, lastName, address, phone;
    private LocalDate birthday;
    private int contactID;
    private File imageFile;

    // This is a Contact constructor(the basic blueprints to building a Contact object)
    public Contact(String firstName, String lastName, LocalDate birthday, String address, String phone) {
        setFirstName(firstName);
        setLastName(lastName);
        setBirthday(birthday);
        setAddress(address);
        setPhone(phone);
        setImageFile(new File("./src/Images/defaultPerson.png")); // this is our default contact image
    }

    // This constructor calls the other constructor in order to set the variables
    // however, by doing this we can now override the default image file
    public Contact(String firstName, String lastName, LocalDate birthday, String address, String phone, File imageFile) throws IOException {
        this(firstName, lastName, birthday, address, phone);
        setImageFile(imageFile);
        copyImageFile();
    }

    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }


    /**
     * This will Validate phone number input
     * and upon receiving valid input will set the variable 'phone'
     * to the validated input
     * format = area code/city/house
     * nxx-xxx-xxxx
     *
     * @param phone
     */
    public void setPhone(String phone) {
        if (phone.matches("[2-9]\\d{2}[.]?\\d{3}[.]?\\d{4}"))
            this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        int contactAge = Period.between(birthday, LocalDate.now()).getYears();

        if (contactAge > 9 && contactAge < 91)
            this.birthday = birthday;
        else
            throw new IllegalArgumentException("Invalid Birthday");
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        if (contactID >= 0)
            this.contactID = contactID;
        else
            throw new IllegalArgumentException("Invalid contactID");
    }


    /**
     * This will copy and rename the image file so
     * it can be stored with a unique name
     */
    public void copyImageFile() throws IOException {
        Path imgFilePath = imageFile.toPath();

        String uniqueImgFileName = getUniqueImgFileName(imageFile.getName());

        Path newFilePath = Paths.get("./src/Images" + uniqueImgFileName);

        Files.copy(imgFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);

        imageFile = new File(newFilePath.toString());
    }


    /**
     * This will take a file name String and ConCat
     * it with a number values to ensure the name is unique in the system
     */
    private String getUniqueImgFileName(String oldFileName) {
        String newFileName;

        SecureRandom ranNumGen = new SecureRandom();

        do {
            newFileName = "";

            for (int tracker = 1; tracker <= 32; tracker++) {
                int addOnValue;

                do {
                    addOnValue = ranNumGen.nextInt(58);
                } while (!validRenameCharacters(addOnValue));

                newFileName = String.format("%s%c", newFileName, addOnValue);
            }
            newFileName += oldFileName;

        } while (!uniqueImgFileExists(newFileName));

        return newFileName;
    }


    /**
     * This will validate that the file name doesn't
     * already exist
     */
    public boolean uniqueImgFileExists(String newFileName) {
        File imgFolder = new File("./src/images/");

        File[] imgFolderContent = imgFolder.listFiles();

        for (File file : imgFolderContent) {
            if (file.getName().equals(newFileName))
                return false;
        }
        return true;
    }

    /**
     * This will validate which ASCII characters are
     * valid for use in renaming the image files
     * the
     */
    public boolean validRenameCharacters(int asciiChartId) {

        if (asciiChartId >= 48 && asciiChartId <= 57) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * This will return a formatted String with a Contacts
     * last name followed by their first name and phone number
     */
    public String toString() {
        return String.format("%s , %s phone#: %s", lastName, firstName, phone);
    }

    /**
     * This will allow our write Contact to be written into
     * the database
     */

    public void addToDB() throws SQLException {

        Connection dbConn = null;
        PreparedStatement preparedStatement = null;

        try {
            dbConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/A1Java?",
                    "root", "root");

            String dbQuery = "INSERT INTO 'Contacts' (first_name, last_name, birthday, address, phone_number, image)"
                    + "VALUES (?,?,?,?,?,?)";

            preparedStatement = dbConn.prepareStatement(dbQuery);

            Date dateBirth = Date.valueOf(birthday);

            //5. Bind the values to the parameters
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setDate(3, dateBirth);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, phone);
            preparedStatement.setString(6, imageFile.getName());


            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();

            if (dbConn != null)
                dbConn.close();
        }
    }
}

package group3.seng3150.entities;
import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "UserAccount")
public class UserAccount {

    //first and last name
    //email address to PK
    //gender, usertype, citizenship to be added
    @Id
    @Column(name = "UserID")
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String UserID;

    @Column(name = "FirstName")
    @Basic(optional = false)
    private String firstName;

    @Column(name = "MiddleNames")
    private String middleNames;

    @Column(name = "LastName")
    @Basic(optional = false)
    private String lastName;

    @Column(name = "Email")
    @Basic(optional = false)
    private String email;

    @Column(name = "Phone")
    @Basic(optional = false)
    private int phone;

    @Column(name = "DateOfBirth")
    @Basic(optional = false)
    private Date dateOfBirth;

    @Column(name = "gender")
    @Basic(optional = false)
    private String gender;

    @Column(name = "Citizenship")
    @Basic(optional = false)
    private String Citizenship;

    @Column(name = "UserType")
    @Basic(optional = false)
    private String UserType;

    //Security needs to be addressed on this
    @Column(name = "Password")
    @Basic(optional = false)
    private String password;

    //Constructors
    public UserAccount() {
    }


    //Getters and Setters
    public String getUserID() {
        return UserID;
    }

//    public void setUserID(String userID) {
//        UserID = userID;
//    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleNames() {
        return middleNames;
    }

    public void setMiddleNames(String middleNames) {
        this.middleNames = middleNames;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCitizenship() {
        return Citizenship;
    }

    public void setCitizenship(String citizenship) {
        Citizenship = citizenship;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Override Methods
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "UserID='" + UserID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleNames='" + middleNames + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", dateOfBirth=" + dateOfBirth +
                ", password='" + password + '\'' +
                '}';
    }
}

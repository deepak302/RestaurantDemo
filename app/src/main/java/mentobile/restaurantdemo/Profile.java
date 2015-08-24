package mentobile.restaurantdemo;

import android.support.v4.app.NavUtils;

/**
 * Created by Deepak Sharma on 7/18/2015.
 */
public class Profile {

    private String FirstName;
    private String LastName;
    private String FullName;
    private String EmailID;
    private String Mobile;
    private String CityName;
    private String Location;
    private String CompanyName;
    private String HouseNo;
    private String ApartmentName;
    private String PostalCode;
    private String Photo;
    private String OtherAddress;
    private String DelIns;

    private static Profile profile = null;

    public static Profile getProfile() {
        if (profile == null) {
            profile = new Profile();
        }
        return profile;
    }

    public Profile() {
    }

    public void setProfile(String firstName, String lastName, String emailID, String mobile,
                           String cityName, String location, String companyName, String houseNo,
                           String apartmentName, String postalCode, String photo, String otherAddress, String delIns) {
        FirstName = firstName;
        LastName = lastName;
        EmailID = emailID;
        Mobile = mobile;
        CityName = cityName;
        Location = location;
        CompanyName = companyName;
        HouseNo = houseNo;
        ApartmentName = apartmentName;
        PostalCode = postalCode;
        Photo = photo;
        OtherAddress = otherAddress;
        DelIns = delIns;
    }

    public static void emptyProfile() {
        if (profile != null) {
            profile = null;
        }
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getFullName() {
        return FullName;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getHouseNo() {
        return HouseNo;
    }

    public void setHouseNo(String houseNo) {
        HouseNo = houseNo;
    }

    public String getApartmentName() {
        return ApartmentName;
    }

    public void setApartmentName(String apartmentName) {
        ApartmentName = apartmentName;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getOtherAddress() {
        return OtherAddress;
    }

    public void setOtherAddress(String otherAddress) {
        OtherAddress = otherAddress;
    }

    public String getDelIns() {
        return DelIns;
    }

    public void setDelIns(String delIns) {
        DelIns = delIns;
    }
}

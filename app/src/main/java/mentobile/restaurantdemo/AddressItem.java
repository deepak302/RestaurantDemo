package mentobile.restaurantdemo;

/**
 * Created by Administrator on 8/26/2015.
 */
public class AddressItem {

    private boolean isAddessSelected;
    private String Email;
    private String FullName;
    private String Phone;
    private String Pincode;
    private String DeliveryAddress;
    private String City;
    private String State;
    private String Landmark;
    private String FullAddress;

//    private static AddressItem addressItem = null;
//
//    public static AddressItem getAddressItem() {
//        if (addressItem == null) {
//            addressItem = new AddressItem();
//        }
//        return addressItem;
//    }
//
//    public AddressItem() {
//
//    }

    public AddressItem(String email, String fullName, String phone, String pincode, String deliveryAddress, String city, String state, String landmark) {
        Email = email;
        FullName = fullName;
        Phone = phone;
        Pincode = pincode;
        DeliveryAddress = deliveryAddress;
        City = city;
        State = state;
        Landmark = landmark;
    }

    public boolean isAddessSelected() {
        return isAddessSelected;
    }

    public void setIsAddessSelected(boolean isAddessSelected) {
        this.isAddessSelected = isAddessSelected;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getDeliveryAddress() {
        return DeliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        DeliveryAddress = deliveryAddress;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public String getFullAddress() {
        return (getFullName() + " , " + getDeliveryAddress() + " , " + getCity() + " , " + getState() +
                " , " + getPincode());
    }
}

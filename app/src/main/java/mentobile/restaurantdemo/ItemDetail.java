package mentobile.restaurantdemo;

/**
 * Created by Administrator on 8/11/2015.
 */
public class ItemDetail {

    private String type;
    private String name;
    private int quantity;
    private int Price;
    static int TotalAmount;
    static int TotalBasketItem;
    static boolean isEditItem;

    public static boolean isEditItem() {
        return isEditItem;
    }

    public static void setIsEditItem(boolean isEditItem) {
        ItemDetail.isEditItem = isEditItem;
    }

    public int getPriceOverQuantity() {
        return (getQuantity() * getPrice());
    }

    public static int getTotalAmount() {
        return TotalAmount;
    }

    public static void setTotalAmount(int mTotal) {
        TotalAmount = mTotal;
    }

    public static int getTotalBasketItem() {
        return TotalBasketItem;
    }

    public static void setTotalBasketItem(int totalBasketItem) {
        TotalBasketItem = totalBasketItem;
    }

    public ItemDetail(String type, String name, int quantity, int price) {
        this.type = type;
        this.name = name;
        this.quantity = quantity;
        Price = price;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
}

package mentobile.restaurantdemo;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 8/11/2015.
 */
public class ItemDetail {

    private String type;
    private String name;
    private int quantity;
    private int Price;

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

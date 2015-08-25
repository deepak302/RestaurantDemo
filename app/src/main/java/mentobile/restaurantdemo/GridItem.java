package mentobile.restaurantdemo;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 8/11/2015.
 */
public class GridItem {

    private Drawable itemICon ;
    private String itemType ;
    private int id ;

    public GridItem(Drawable itemICon, String itemType, int id) {
        this.itemICon = itemICon;
        this.itemType = itemType;
        this.id = id;
    }

    public Drawable getItemICon() {
        return itemICon;
    }

    public void setItemICon(Drawable itemICon) {
        this.itemICon = itemICon;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

package mentobile.restaurantdemo;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 8/11/2015.map
 */
public class GridItem {

    private Bitmap itemICon ;
    private String itemType ;
    private int id ;
    private String imageName ;

    public GridItem(Bitmap itemICon, String itemType, int id, String imagename) {
        this.itemICon = itemICon;
        this.itemType = itemType;
        this.id = id;
        this.imageName = imagename ;
    }

    public Bitmap getItemICon() {
        return itemICon;
    }

    public void setItemICon(Bitmap itemICon) {
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}

package mentobile.restaurantdemo;

/**
 * 
 * @author manish.s
 *
 */

public class Items {
	private int image_ResID;
	private String title;
	private String description;
	private boolean isChecked;

	public Items(int image_ResID, String title, String description,
			boolean isChecked) {
		super();
		this.image_ResID = image_ResID;
		this.title = title;
		this.description = description;
		this.isChecked = isChecked;
	}

	public int getImage() {
		return image_ResID;
	}

	public void setImage(int image_ResID) {
		this.image_ResID = image_ResID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

}

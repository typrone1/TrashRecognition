package devfest.hackathon.trashrecognition.common;

public class Item {
    private String name;
    private int image;
    private int itemsLove;
    private String price;
    private String itemsSell;
    private String priorityItem;
    private String itemAdress;

    public Item(String name, int image, int itemsLove, String price, String itemsSell, String priorityItem, String itemAdress) {
        this.name = name;
        this.image = image;
        this.itemsLove = itemsLove;
        this.price = price;
        this.itemsSell = itemsSell;
        this.priorityItem = priorityItem;
        this.itemAdress = itemAdress;
    }

    public boolean isLove(){
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getItemsLove() {
        return itemsLove;
    }

    public void setItemsLove(int itemsLove) {
        this.itemsLove = itemsLove;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItemsSell() {
        return itemsSell;
    }

    public void setItemsSell(String itemsSell) {
        this.itemsSell = itemsSell;
    }

    public String getPriorityItem() {
        return priorityItem;
    }

    public void setPriorityItem(String priorityItem) {
        this.priorityItem = priorityItem;
    }

    public String getItemAdress() {
        return itemAdress;
    }

    public void setItemAdress(String itemAdress) {
        this.itemAdress = itemAdress;
    }
}

package devfest.hackathon.trashrecognition.common;

public class Product {
    String nameLabel;
    int imageLabel;

    String addressLabel;
    String phoneProduct;
    String nameUser;
    String amount;

    public Product() {
    }

    public Product(String addressLabel, String phoneProduct, String nameUser, String amount) {
        this.amount = amount;
        this.nameUser = nameUser;
        this.addressLabel = addressLabel;
        this.phoneProduct = phoneProduct;
    }

    public String getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(String nameLabel) {
        this.nameLabel = nameLabel;
    }

    public int getImageLabel() {
        return imageLabel;
    }

    public void setImageLabel(int imageLabel) {
        this.imageLabel = imageLabel;
    }

    public String getAddressLabel() {
        return addressLabel;
    }

    public void setAddressLabel(String addressLabel) {
        this.addressLabel = addressLabel;
    }

    public String getPhoneProduct() {
        return phoneProduct;
    }

    public void setPhoneProduct(String phoneProduct) {
        this.phoneProduct = phoneProduct;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

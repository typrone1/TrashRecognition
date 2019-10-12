package devfest.hackathon.trashrecognition.common;

import android.graphics.Bitmap;

public class Label {
    private long idLabel;
    private Bitmap imageLabel;
    private String nameLabel;

    public Label(long idLabel, Bitmap imageLabel, String nameLabel) {
        this.idLabel = idLabel;
        this.imageLabel = imageLabel;
        this.nameLabel = nameLabel;
    }

    public long getIdLabel() {
        return idLabel;
    }

    public void setIdLabel(long idLabel) {
        this.idLabel = idLabel;
    }

    public Bitmap getImageLabel() {
        return imageLabel;
    }

    public void setImageLabel(Bitmap imageLabel) {
        this.imageLabel = imageLabel;
    }

    public String getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(String nameLabel) {
        this.nameLabel = nameLabel;
    }
}

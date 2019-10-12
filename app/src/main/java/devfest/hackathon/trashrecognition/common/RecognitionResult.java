package devfest.hackathon.trashrecognition.common;

public class RecognitionResult {
    private String label;
    private String title;
    private String typeOfTrashText;
    private String description;
    private boolean droppable = true;
    private boolean sellable = true;
    private String imageGeneralUrl;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTypeOfTrashText() {
        return typeOfTrashText;
    }

    public void setTypeOfTrashText(String typeOfTrashText) {
        this.typeOfTrashText = typeOfTrashText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDroppable() {
        return droppable;
    }

    public void setDroppable(boolean droppable) {
        this.droppable = droppable;
    }

    public boolean isSellable() {
        return sellable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSellable(boolean sellable) {
        this.sellable = sellable;
    }

    public String getImageGeneralUrl() {
        return imageGeneralUrl;
    }

    public void setImageGeneralUrl(String imageGeneralUrl) {
        this.imageGeneralUrl = imageGeneralUrl;
    }
}

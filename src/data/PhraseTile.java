package data;
import javafx.scene.image.Image;

public class PhraseTile {
    
    private String phrase;
    private transient Image icon;

    public PhraseTile(String phrase, Image icon) {
        this.phrase = phrase;
        this.icon = icon;
    }

    public String getPhrase() {
        return this.phrase;
    }
    public Image getIcon() { 
        return icon; 
    }
}

/* 
    Vivaan Echambadi
    1/22/2026

    PhraseTile() class will represent a phrase tile with a phrase and an associated icon image. It will provide getter methods
    to access the phrase and icon.

*/

// importing packages
package data;
import javafx.scene.image.Image;

// PhraseTile class definition
public class PhraseTile {
    
    // private attributes for phrase and icon
    private String phrase;
    private transient Image icon;

    // constructor to initialize phrase tile with a phrase and an icon
    public PhraseTile(String phrase, Image icon) {
        // setting the phrase and icon attributes
        this.phrase = phrase;
        this.icon = icon;
    }

    // getPhrase() method will return the phrase
    public String getPhrase() {
        return this.phrase;
    }
    // getIcon() method will return the icon
    public Image getIcon() { 
        return icon; 
    }
}

package business;

public class PhrasePair {
    private String phrase;
    private String meaning;

    public PhrasePair(String phrase, String meaning) {
        this.phrase = phrase;
        this.meaning = meaning;
    }

    public String getPhrase() {
        return phrase;
    }

    public String getMeaning() {
        return meaning;
    }

    public String toString() {
        return phrase + ": " + meaning;
    } 
}

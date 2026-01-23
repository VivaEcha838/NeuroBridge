/* 
    Vivaan Echambadi
    1/22/2026

    PhraseSuggestion() class will represent a suggested phrase along with its score and the reason for the suggestion.
    It will provide getter methods to access the phrase, score, and reason, as well as a toString() method for easy display.

*/

// importing package
package business;

// PhraseSuggestion class definition
public class PhraseSuggestion {

    // private attributes for phrase, score, and reason
    private String phrase;
    private double score;
    private String reason;

    // constructor to initialize phrase suggestion with a phrase, score, and reason
    public PhraseSuggestion(String phrase, double score, String reason) {
        this.phrase = phrase;
        this.score = score;
        this.reason = reason;
    }

    // getPhrase() method will return the suggested phrase
    public String getPhrase() {
        return phrase;
    }

    // getScore() method will return the score of the suggestion
    public double getScore() {
        return score;
    }

    // getReason() method will return the reason for the suggestion
    public String getReason() {
        return reason;
    }

    // toString() method will return a string representation of the phrase suggestion
    public String toString() {
        // returning the phrase along with its score and reason
        return phrase + " (Score: " + score + ", Reason: " + reason + ")";
    }
}

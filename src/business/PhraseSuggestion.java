package business;

public class PhraseSuggestion {
    private String phrase;
    private double score;
    private String reason;

    public PhraseSuggestion(String phrase, double score, String reason) {
        this.phrase = phrase;
        this.score = score;
        this.reason = reason;
    }

    public String getPhrase() {
        return phrase;
    }

    public double getScore() {
        return score;
    }

    public String getReason() {
        return reason;
    }

    public String toString() {
        return phrase + " (Score: " + score + ", Reason: " + reason + ")";
    }
}

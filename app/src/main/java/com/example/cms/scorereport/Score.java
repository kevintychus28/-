package com.example.cms.scorereport;

public class Score {
    private String cName;
    private String cScore;

    public Score() {
    }

    public Score(String cName, String cScore) {
        this.cName = cName;
        this.cScore = cScore;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcScore() {
        return cScore;
    }

    public void setcScore(String cScore) {
        this.cScore = cScore;
    }
}

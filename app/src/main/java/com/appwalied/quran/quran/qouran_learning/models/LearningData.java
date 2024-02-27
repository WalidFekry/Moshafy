package com.appwalied.quran.quran.qouran_learning.models;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class LearningData implements Serializable {

    private Reciter selectedReciter;
    private SoraDetails soraDetails;
    private int selectedSoraNumber = 1;
    private int selectedFrom = 1;
    private int selectedTo = 1;
    private int repeatAya = 0;
    private int repeatSora = 0;

    public LearningData(Reciter selectedReciter, SoraDetails soraDetails, int selectedSoraNumber, int selectedFrom, int selectedTo, int repeatAya, int repeatSora) {
        this.selectedReciter = selectedReciter;
        this.soraDetails = soraDetails;
        this.selectedSoraNumber = selectedSoraNumber;
        this.selectedFrom = selectedFrom;
        this.selectedTo = selectedTo;
        this.repeatAya = repeatAya;
        this.repeatSora = repeatSora;
    }

    public LearningData() {
    }

    public Reciter getSelectedReciter() {
        return selectedReciter;
    }

    public void setSelectedReciter(Reciter selectedReciter) {
        this.selectedReciter = selectedReciter;
    }

    public SoraDetails getSoraDetails() {
        return soraDetails;
    }

    public void setSoraDetails(SoraDetails soraDetails) {
        this.soraDetails = soraDetails;
    }

    public int getSelectedSoraNumber() {
        return selectedSoraNumber;
    }

    public void setSelectedSoraNumber(int selectedSoraNumber) {
        this.selectedSoraNumber = selectedSoraNumber;
    }

    public int getSelectedFrom() {
        return selectedFrom;
    }

    public void setSelectedFrom(int selectedFrom) {
        this.selectedFrom = selectedFrom;
    }

    public int getSelectedTo() {
        return selectedTo;
    }

    public void setSelectedTo(int selectedTo) {
        this.selectedTo = selectedTo;
    }

    public int getRepeatAya() {
        return repeatAya;
    }

    public void setRepeatAya(int repeatAya) {
        this.repeatAya = repeatAya;
    }

    public int getRepeatSora() {
        return repeatSora;
    }

    public void setRepeatSora(int repeatSora) {
        this.repeatSora = repeatSora;
    }
}

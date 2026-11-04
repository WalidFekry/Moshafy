package com.appwalied.quran.azkar;

public class AzkarModel {
    private String id;
    private String zikrText;
    private String virtueText;
    private int targetCount;
    private int currentCount;
    private String categoryName;

    public AzkarModel(String id, String zikrText, String virtueText, int targetCount, String categoryName) {
        this.id = id;
        this.zikrText = zikrText != null ? zikrText.trim() : "";
        this.virtueText = virtueText != null ? virtueText.trim() : "";
        this.targetCount = Math.max(1, targetCount);
        this.currentCount = 0;
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZikrText() {
        return zikrText;
    }

    public void setZikrText(String zikrText) {
        this.zikrText = zikrText;
    }

    public String getVirtueText() {
        return virtueText;
    }

    public void setVirtueText(String virtueText) {
        this.virtueText = virtueText;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = Math.min(Math.max(0, currentCount), targetCount);
    }

    public boolean isCompleted() {
        return currentCount >= targetCount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void incrementCount() {
        if (currentCount < targetCount) {
            currentCount++;
        }
    }

    public void resetCount() {
        currentCount = 0;
    }
}

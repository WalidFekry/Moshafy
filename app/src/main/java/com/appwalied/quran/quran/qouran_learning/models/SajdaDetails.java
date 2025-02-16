package com.appwalied.quran.quran.qouran_learning.models;

import java.io.Serializable;
import java.util.Map;

public class SajdaDetails implements Serializable {

    private Boolean recommended;
    private Boolean obligatory;

    public SajdaDetails(Map<String, Object> json) {
        if (json.containsKey("recommended")) {
            this.recommended = (Boolean) json.get("recommended");
        }
        if (json.containsKey("obligatory")) {
            this.obligatory = (Boolean) json.get("obligatory");
        }
    }

    public Boolean isRecommended() {
        return recommended;
    }

    public Boolean isObligatory() {
        return obligatory;
    }
}

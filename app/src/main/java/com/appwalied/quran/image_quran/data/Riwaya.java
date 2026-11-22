package com.appwalied.quran.image_quran.data;

public enum Riwaya {
    HAFS("hafs", "مصحف حفص عن عاصم", "https://post.walid-fekry.com/quran/"),
    WARSH("warsh", "مصحف ورش عن نافع", "https://post.walid-fekry.com/data/quran_warsh/");

    private final String key;
    private final String displayName;
    private final String baseUrl;

    Riwaya(String key, String displayName, String baseUrl) {
        this.key = key;
        this.displayName = displayName;
        this.baseUrl = baseUrl;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public static Riwaya fromKey(String key) {
        if (key != null && key.equalsIgnoreCase(WARSH.key)) {
            return WARSH;
        }
        return HAFS;
    }
}

package com.appwalied.quran.quran_listening.download;

public enum DownloadStatus {
    NOT_DOWNLOADED(0),
    QUEUED(1),
    DOWNLOADING(2),
    PAUSED(3),
    COMPLETED(4),
    FAILED(5),
    CANCELLED(6),
    VERIFYING(7);

    private final int value;

    DownloadStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DownloadStatus fromValue(int value) {
        for (DownloadStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        return NOT_DOWNLOADED;
    }
}

package com.appwalied.quran.quran_listening.download;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "downloaded_surahs")
public class DownloadedSurahEntity {

    @PrimaryKey
    @NonNull
    private String downloadId;

    private int reciterId;
    private String reciterName;
    private int surahId;
    private String surahName;
    private String remoteUrl;
    private String localFilePath;
    private long fileSize;
    private long downloadedBytes;
    private int downloadStatus;
    private long downloadDate;
    private long lastModified;
    private String errorMessage;

    public DownloadedSurahEntity() {
        this.downloadId = "";
    }

    public DownloadedSurahEntity(int reciterId, String reciterName, int surahId, String surahName, String remoteUrl, String localFilePath) {
        this.downloadId = reciterId + "_" + surahId;
        this.reciterId = reciterId;
        this.reciterName = reciterName;
        this.surahId = surahId;
        this.surahName = surahName;
        this.remoteUrl = remoteUrl;
        this.localFilePath = localFilePath;
        this.downloadStatus = DownloadStatus.NOT_DOWNLOADED.getValue();
        this.downloadDate = System.currentTimeMillis();
        this.lastModified = System.currentTimeMillis();
    }

    @NonNull
    public String getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(@NonNull String downloadId) {
        this.downloadId = downloadId;
    }

    public int getReciterId() {
        return reciterId;
    }

    public void setReciterId(int reciterId) {
        this.reciterId = reciterId;
    }

    public String getReciterName() {
        return reciterName != null ? reciterName : "";
    }

    public void setReciterName(String reciterName) {
        this.reciterName = reciterName;
    }

    public int getSurahId() {
        return surahId;
    }

    public void setSurahId(int surahId) {
        this.surahId = surahId;
    }

    public String getSurahName() {
        return surahName != null ? surahName : "";
    }

    public void setSurahName(String surahName) {
        this.surahName = surahName;
    }

    public String getRemoteUrl() {
        return remoteUrl != null ? remoteUrl : "";
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getLocalFilePath() {
        return localFilePath != null ? localFilePath : "";
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getDownloadedBytes() {
        return downloadedBytes;
    }

    public void setDownloadedBytes(long downloadedBytes) {
        this.downloadedBytes = downloadedBytes;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public DownloadStatus getStatusEnum() {
        return DownloadStatus.fromValue(downloadStatus);
    }

    public void setStatusEnum(DownloadStatus status) {
        this.downloadStatus = status.getValue();
    }

    public long getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(long downloadDate) {
        this.downloadDate = downloadDate;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getProgressPercentage() {
        if (fileSize <= 0) return 0;
        int pct = (int) ((downloadedBytes * 100) / fileSize);
        return Math.min(100, Math.max(0, pct));
    }
}

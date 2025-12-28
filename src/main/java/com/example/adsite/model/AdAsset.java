package com.example.adsite.model;

public class AdAsset {
    public enum Format {
        IMAGE,
        VIDEO,
        UNKNOWN
    }

    private final long id;
    private final String ownerId;
    private final String fileName;
    private final String contentType;
    private final String interestTag;

    public AdAsset(long id, String ownerId, String fileName, String contentType, String interestTag) {
        this.id = id;
        this.ownerId = ownerId;
        this.fileName = fileName;
        this.contentType = contentType;
        this.interestTag = interestTag;
    }

    public long getId() { return id; }
    public String getOwnerId() { return ownerId; }
    public String getFileName() { return fileName; }
    public String getContentType() { return contentType; }
    public String getInterestTag() { return interestTag; }
}

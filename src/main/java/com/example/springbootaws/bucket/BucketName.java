package com.example.springbootaws.bucket;

public enum BucketName {
    PROFILE_IMAGE("spring-aws-image-upload-123");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}

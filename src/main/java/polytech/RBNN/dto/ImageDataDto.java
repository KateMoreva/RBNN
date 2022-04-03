package polytech.RBNN.dto;

public class ImageDataDto {

    private final Long timestamp;
    private final String imageUrl;

    public ImageDataDto(Long timestamp, String imageUrl) {
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

package iclass.rajat_pc.example.com.modereg;

public class NewPost {
    private String title;
    private String message;
    private String downloadUri;
    private String senderName;


    public NewPost() {
    }

    public NewPost(String title, String message, String downloadUri,String senderName) {
        this.senderName = senderName;
        this.title = title;
        this.message = message;
        this.downloadUri = downloadUri;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public String getSenderName() {
        return senderName;
    }
}

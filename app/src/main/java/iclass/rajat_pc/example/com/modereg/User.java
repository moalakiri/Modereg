package iclass.rajat_pc.example.com.modereg;

public class User {
    private String userId;
    private String userName;
    private String email;

    private boolean faculty;

    public User() {
    }

    public User(String userId,String userName, String email, boolean faculty) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.faculty = faculty;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isFaculty() {
        return faculty;
    }

    public String getUserId() {
        return userId;
    }
}
package model;

import java.util.Date;
import java.util.List;

public class Post extends LectureContent {
    // Additional Fields
    private Date uploadTime;
    private List<String> comments;
    private String lectureId;

    // Constructor
    private Post() {
        super();
    }
    private Post(String id, String title, int week, Date deadline,
                 String type, List<String> files, String detail,
                 Date uploadTime, List<String> comments, String lectureId) {
        super(id, title, week, deadline, type, files, detail);
        this.uploadTime = uploadTime;
        this.comments = comments;
        this.lectureId = lectureId;
    }

    // Named Constructor
    public static Post makePost() {
        // named constructor
        // TODO: implement
        return new Post(null, null, 0, null, null, null, null, null, null, null);
    }

    public static Post makePost(String id, String title, int week, Date deadline, String type, List<String> files, String detail, Date uploadTime, List<String> comments, String lectureId) {
        return new Post(id, title, week, deadline, type, files, detail, uploadTime, comments, lectureId);
    }

    // Methods (If additional methods are needed for Post, add them here)

    // getter
    public String getLectureId() { return this.lectureId; }
    public Date getUploadTime() { return uploadTime; }
    public List<String> getComments() {
        return comments;
    }
}


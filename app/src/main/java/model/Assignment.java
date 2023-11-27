package model;

import java.util.Date;
import java.util.List;

public class Assignment extends LectureContent {
    // Additional Fields
    private Date uploadTime;
    private Date deadline;
    private List<String> submissions;
    private String lectureId;

    // Constructor
    private Assignment() {
        super();
    }

    private Assignment(String id, String title, int week, Date deadline,
                       String type, List<String> files, String detail,
                       Date uploadTime, Date deadlineDate, List<String> submissions, String lectureId) {
        super(id, title, week, deadline, type, files, detail);
        this.uploadTime = uploadTime;
        this.deadline = deadlineDate;
        this.submissions = submissions;
        this.lectureId = lectureId;
    }

    // Named Constructor
    public static Assignment makeAssignment() {
        // named constructor
        // TODO: implement
        return new Assignment(null, null, 0, null, null, null, null, null, null, null, null);
    }

    public static Assignment makeAssignment(String id, String title, int week, Date deadline,
                                            String type, List<String> files, String detail,
                                            Date uploadTime, Date deadlineDate, List<String> submissions, String lectureId) {
        // named constructor
        // TODO: implement
        return new Assignment(id, title, week, deadline, type, files, detail, uploadTime, deadlineDate, submissions, lectureId);
    }

    // Methods (If additional methods are needed for Assignment, add them here)

    // getter
    public Date getUploadTime() {
        return uploadTime;
    }

    public Date getDeadline() {
        return deadline;
    }
    public String getLectureId() { return this.lectureId; }

    public List<String> getSubmissions() {
        return submissions;
    }
}


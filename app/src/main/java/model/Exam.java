package model;

import java.util.Date;
import java.util.List;

public class Exam extends LectureContent {
    // Additional Fields
    private Date date;
    private String location;

    private String lectureId;
    // Constructor
    private Exam() {}
    private Exam(String id, String title, int week, Date deadline,
                 String type, List<String> files, String detail,
                 Date date, String location, String lectureId) {
        super(id, title, week, deadline, type, files, detail);
        this.date = date;
        this.location = location;
        this.lectureId = lectureId;
    }

    // Named Constructor
    public static Exam makeExam() {
        // named constructor
        // TODO: implement
        return new Exam(null, null, 0, null, null, null, null, null, null, null);
    }
    public static Exam makeExam(String id, String title, int week, Date deadline,
                                String type, List<String> files, String detail,
                                Date date, String location, String lectureId) {
        return new Exam(id, title, week, deadline, type, files, detail, date, location, lectureId);
    }

    // Methods (If additional methods are needed for Exam, add them here)

    // getter
    public Date getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getLectureId() { return this.lectureId; }
}


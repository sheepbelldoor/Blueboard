package model;

import java.util.Date;
import java.util.List;

public class LectureContent {
    // Fields
    private String id;
    private String title;
    private int week;
    private Date deadline;
    private String type;
    private String files;
    private String detail;

    // Constructor
    protected LectureContent() {}
    protected LectureContent(String id, String title, int week, Date deadline,
                           String type, String files, String detail) {
        this.id = id;
        this.title = title;
        this.week = week;
        this.deadline = deadline;
        this.type = type;
        this.files = files;
        this.detail = detail;
    }

    // Named Constructor (Factory Design Pattern)
    public static LectureContent makeLectureContent() {
        // named constructor (factory function)
        // TODO: implement
        return new LectureContent(null, null, 0, null, null, null, null);
    }

    public static LectureContent makeLectureContent(String id, String title, int week, Date deadline, String type, String files, String detail) {
        // named constructor (factory function)
        // TODO: implement
        return new LectureContent(id, title, week, deadline, type, files, detail);
    }

    // Methods
    public void addFile(String fileTitle, Date date, String fileName) {
        // 수업자료 유형의 content일 경우. 제목, 날짜, 파일명이 argument로 제공된다.
        // TODO: implement
    }

    public void removeContent() {
        // content 삭제
        // TODO: implement
    }

    // getter
    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public int getWeek() {
        return this.week;
    }

    public Date getDeadline() {
        return this.deadline;
    }

    public String getType() {
        return this.type;
    }

    public String getFiles() { return this.files; }

    public String getDetail() {
        return this.detail;
    }
}


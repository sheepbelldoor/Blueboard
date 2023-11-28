package model;

import java.util.Date;
import java.util.List;

public class Material extends LectureContent {
    // Additional Fields
    private Date uploadTime;

    // Constructor
    private Material() {
        super();
    }
    private Material(String id, String title, int week, Date deadline,
                     String type, String files, String detail,
                     Date uploadTime) {
        super(id, title, week, deadline, type, files, detail);
        this.uploadTime = uploadTime;
    }

    // Named Constructor
    public static Material makeMaterial() {
        // named constructor
        // TODO: implement
        return new Material(null, null, 0, null, null, null, null, null);
    }

    // Methods (If additional methods are needed for Material, add them here)

    // getter
    public Date getUploadTime() {
        return uploadTime;
    }
}


package utils;

import android.content.Context;
import android.widget.LinearLayout;

import java.io.File;
import java.util.List;

import model.User;

public interface Utilities {
    boolean isAdmin(User user);
    boolean fileAvail(File file, long sizeLimit, List<String> formats);
    boolean lengthAvail(String str, int length);
    void sortBy(List<String> list, Utils.Sort order);
    void filterBy();
    void search(List<String> list, String searchString);
    static void gotoPage(String page){};
    void showErrMsg(Context context, String errorMessage);
    void pushAlarm(Context context, String channelId, int id, String title, String body);
    static String masking(String personalInfo) { return null; }
    void pagination(Context context, LinearLayout containerLayout, List<String> pageItem, int currentPage);
}
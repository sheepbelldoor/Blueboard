package utils;

import model.Message;

public interface MyCallback {
    void onSuccess(Object object);
    void onFailure(Exception e);
}

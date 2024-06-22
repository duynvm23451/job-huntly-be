package com.phenikaa.jobhuntly.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String objectName, Integer id) {
        super("Không tìm thấy " + objectName + " với id=" + id);
    }

    public ObjectNotFoundException(String objectName, String propertyName, String value) {
        super("Không tìm thấy " + objectName + " với " + propertyName + "=" + value);
    }
}

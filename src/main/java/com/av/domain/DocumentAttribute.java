package com.av.domain;

/**
 * Created by alexey on 17.10.15.
 */
public class DocumentAttribute extends AbstractValue {

    // private int id;
    // private String attributeCode;
    //  private String attributeName;
    private String path;
    private String fieldClass;
    private String fieldCode;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(String fieldClass) {
        this.fieldClass = fieldClass;
    }

    @Override
    public String toString() {
        return "DocumentAttribute{" +
                "id=" + super.getId() +
                ", Code='" + super.getCode() + '\'' +
                ",  Name='" + super.getName() + '\'' +
                ", path='" + path + '\'' +
                ", fieldClass='" + fieldClass + '\'' +
                ", fieldCode='" + fieldCode + '\'' +
                '}';
    }
}

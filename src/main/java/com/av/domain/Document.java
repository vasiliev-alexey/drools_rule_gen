package com.av.domain;

import java.util.List;

/**
 * Created by alexey on 17.10.15.
 */
public class Document {

    private long id;
    private String code;
    private String name;
    private List<DocumentAttribute> documentAttributeList;

    private String className;
    private String packagePath;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DocumentAttribute> getDocumentAttributeList() {
        return documentAttributeList;
    }

    public void setDocumentAttributeList(List<DocumentAttribute> documentAttributeList) {
        this.documentAttributeList = documentAttributeList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document)) return false;

        Document document = (Document) o;

        if (id != document.id) return false;
        if (code != null ? !code.equals(document.code) : document.code != null) return false;
        if (name != null ? !name.equals(document.name) : document.name != null) return false;
        return !(documentAttributeList != null ? !documentAttributeList.equals(document.documentAttributeList) : document.documentAttributeList != null);

    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", documentAttributeList=" + documentAttributeList +
                ", className='" + className + '\'' +
                ", packagePath='" + packagePath + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (documentAttributeList != null ? documentAttributeList.hashCode() : 0);
        return result;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }
}

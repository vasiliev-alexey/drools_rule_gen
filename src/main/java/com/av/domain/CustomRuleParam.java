package com.av.domain;

import java.util.Date;

/**
 * Created by vasil on 20.10.2015.
 */
public class CustomRuleParam  implements  Comparable<CustomRuleParam>{

    private long id;
    private String code;
    private String name;
    private DocumentAttribute attr;
    private Number numParam;
    private String stringParam;
    private Date dateParam;
private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

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

    public DocumentAttribute getAttr() {
        return attr;
    }

    public void setAttr(DocumentAttribute attr) {
        this.attr = attr;
    }

    public Number getNumParam() {
        return numParam;
    }

    public void setNumParam(Number numParam) {
        this.numParam = numParam;
    }

    public String getStringParam() {
        return stringParam;
    }

    public void setStringParam(String stringParam) {
        this.stringParam = stringParam;
    }

    public Date getDateParam() {
        return dateParam;
    }

    public void setDateParam(Date dateParam) {
        this.dateParam = dateParam;
    }

    @Override
    public String toString() {
        return "CustomRuleParam{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", attr=" + attr +
                ", numParam=" + numParam +
                ", stringParam='" + stringParam + '\'' +
                ", dateParam=" + dateParam +
                ", position=" + position +
                '}';
    }

    public int compareTo(CustomRuleParam o) {


        if (this.getPosition()  < o.getPosition()) {
            return  -1;
        } else   {
            return 1;
        }



    }
}

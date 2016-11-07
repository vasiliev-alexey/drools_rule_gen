package com.av.domain;

import java.util.List;

/**
 * Created by alexey on 17.10.15.
 */
public class Condition {

    private long id;
    private String code;
    private String name;
    private List<ConditionLine> conditionLineList;

    @Override
    public String toString() {
        return "Condition{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", conditionLineList=" + conditionLineList +
                '}';
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

    public List<ConditionLine> getConditionLineList() {
        return conditionLineList;
    }

    public void setConditionLineList(List<ConditionLine> conditionLineList) {
        this.conditionLineList = conditionLineList;
    }
}

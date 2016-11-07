package com.av.domain;

/**
 * Created by alexey on 22.10.15.
 */
public class GroupRuleItem implements  Comparable<GroupRuleItem>{

    private long id;
    private int userSeq;
    private AbstractValue value;
    private Condition condition;
    private String valueType;

    public int getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(int userSeq) {
        this.userSeq = userSeq;
    }

    public AbstractValue getValue() {
        return value;
    }

    public void setValue(AbstractValue value) {
        this.value = value;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }


    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    @Override
    public String toString() {
        return "GroupRuleItem{" +
                "id=" + id +
                ", userSeq=" + userSeq +
                ", value=" + value +
                ", condition=" + condition +
                ", valueType='" + valueType + '\'' +
                '}';
    }

    public int compareTo(GroupRuleItem o) {

        if (o.getUserSeq() > this.getUserSeq()) {
            return  -1;
        } else {
            return  1;
        }


    }
}

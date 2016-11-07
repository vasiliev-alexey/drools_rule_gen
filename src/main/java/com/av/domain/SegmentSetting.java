package com.av.domain;

/**
 * Created by alexey on 17.10.15.
 */
public class SegmentSetting implements Comparable<SegmentSetting> {

    private long id;
    private String ruleType;
    private int segmentNum;
    private String typeEntry;
    private AbstractValue value;


    @Override
    public String toString() {
        return "SegmentSetting{" +
                "id=" + id +
                ", ruleType='" + ruleType + '\'' +
                ", segmentNum=" + segmentNum +
                ", typeEntry='" + typeEntry + '\'' +
                ", value=" + value +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public int getSegmentNum() {
        return segmentNum;
    }

    public void setSegmentNum(int segmentNum) {
        this.segmentNum = segmentNum;
    }

    public String getTypeEntry() {
        return typeEntry;
    }

    public void setTypeEntry(String typeEntry) {
        this.typeEntry = typeEntry;
    }

    public AbstractValue getValue() {
        return value;
    }

    public void setValue(AbstractValue value) {
        this.value = value;
    }


    public int compareTo(SegmentSetting o) {

        if (o.getTypeEntry().equals(this.getTypeEntry())) {
            return  -1 ;
        } else {
           return this.getSegmentNum() - o.getSegmentNum();
        }


    }
}

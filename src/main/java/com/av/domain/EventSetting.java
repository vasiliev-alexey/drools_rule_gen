package com.av.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexey on 17.10.15.
 */
public class EventSetting {

    private long id;
    private String code;
    private Condition condition;
    private boolean enabled;
    private List<SegmentSetting> segmentSettingList;

    public EventSetting() {

        super();
        segmentSettingList = new ArrayList<SegmentSetting>();
        for (int i = 1; i < 4; i++) {

            SegmentSetting ss = new SegmentSetting();
            ss.setSegmentNum(i);
            ss.setTypeEntry("D");
            segmentSettingList.add(ss);
            ss = new SegmentSetting();
            ss.setSegmentNum(i);
            ss.setTypeEntry("C");
            segmentSettingList.add(ss);
        }


    }


    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public List<SegmentSetting> getSegmentSettingList() {
        return segmentSettingList;
    }

    public void setSegmentSettingList(List<SegmentSetting> segmentSettingList) {
        this.segmentSettingList = segmentSettingList;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    @Override
    public String toString() {
        return "EventSetting{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", condition=" + condition +
                ", enabled=" + enabled +
                ", segmentSettingList=" + segmentSettingList +
                '}';
    }
}

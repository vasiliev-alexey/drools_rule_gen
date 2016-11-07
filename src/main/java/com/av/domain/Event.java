package com.av.domain;

import java.util.List;

/**
 * Created by alexey on 17.10.15.
 */
public class Event {

    private long id;
    private String code;
    private String name;
    private Document doc;
    private boolean enabled;
    private List<EventSetting> eventSettingList;

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

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EventSetting> getEventSettingList() {
        return eventSettingList;
    }

    public void setEventSettingList(List<EventSetting> eventSettingList) {
        this.eventSettingList = eventSettingList;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", doc=" + doc +
                ", enabled=" + enabled +
                ", eventSettingList=" + eventSettingList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (enabled != event.enabled) return false;
        if (code != null ? !code.equals(event.code) : event.code != null) return false;
        if (name != null ? !name.equals(event.name) : event.name != null) return false;
        if (doc != null ? !doc.equals(event.doc) : event.doc != null) return false;
        return !(eventSettingList != null ? !eventSettingList.equals(event.eventSettingList) : event.eventSettingList != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (doc != null ? doc.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (eventSettingList != null ? eventSettingList.hashCode() : 0);
        return result;
    }
}

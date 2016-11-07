package com.av.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexey on 22.10.15.
 */
public class GroupRule extends AbstractValue {

    private List<GroupRuleItem> items = new ArrayList<GroupRuleItem>();
    private int segNum;
    private Document doc;
    private boolean enabled;


    public void setItems(List<GroupRuleItem> items) {
        this.items = items;
    }

    public void setSegNum(int segNum) {
        this.segNum = segNum;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public List<GroupRuleItem> getItems() {
        return items;
    }

    public int getSegNum() {
        return segNum;
    }

    public Document getDoc() {
        return doc;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    @Override
    public String toString() {
        return "GroupRule{" +
                "items=" + items +
                ", segNum=" + segNum +
                ", doc=" + doc +
                ", enabled=" + enabled +
                '}';
    }
}

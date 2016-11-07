package com.av.domain;

import java.util.List;

/**
 * Created by vasil on 20.10.2015.
 */
public class CustomRule extends AbstractValue {

    private long id;
    private String API;
    private String returnType;
    private Document doc;
    private List<CustomRuleParam> customRuleParamList;


    @Override
    public String toString() {
        return "CustomRule{" +
                "id=" + id +
                ", API='" + API + '\'' +
                ", returnType='" + returnType + '\'' +
                ", doc=" + doc +
                ", customRuleParamList=" + customRuleParamList +
                '}';
    }

    public String getAPI() {
        return API;
    }

    public void setAPI(String API) {
        this.API = API;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<CustomRuleParam> getCustomRuleParamList() {
        return customRuleParamList;
    }

    public void setCustomRuleParamList(List<CustomRuleParam> customRuleParamList) {
        this.customRuleParamList = customRuleParamList;
    }
}

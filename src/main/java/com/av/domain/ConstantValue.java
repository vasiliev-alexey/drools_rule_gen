package com.av.domain;

/**
 * Created by vasil on 20.10.2015.
 */
public class ConstantValue extends AbstractValue {

    public ConstantValue() {
        super();
        setType("CONSTANT");
    }

    @Override
    public String toString() {
        return "ConstantValue{" +
                "id=" + super.getId() +
                ", code='" + super.getCode() + '\'' +
                ", name='" + super.getName() + '\'' +
                ", type='" + super.getType() + '\'' +
                '}';
    }


}

package com.av.domain;

/**
 * Created by alexey on 17.10.15.
 */
public class ConditionLine  implements  Comparable<ConditionLine> {
    public int compareTo(ConditionLine o) {

        if (this.getUserSeq()  < o.getUserSeq()) {
            return  -1;
        } else   {
            return 1;
        }


    }

    private long id;
    private int userSeq;
    private String leftObjectType;
    private String leftBracket;
    private AbstractValue leftValue;
    private String expression;
    private AbstractValue rightValue;
    private String rightObjectType;
    private String rightBracket;
    private String operator;

    @Override
    public String toString() {
        return "ConditionLine{" +
                "id=" + id +
                ", userSeq=" + userSeq +
                ", leftObjectType='" + leftObjectType + '\'' +
                ", leftBracket='" + leftBracket + '\'' +
                ", leftValue=" + leftValue +
                ", expression='" + expression + '\'' +
                ", rightValue=" + rightValue +
                ", rightObjectType='" + rightObjectType + '\'' +
                ", rightBracket='" + rightBracket + '\'' +
                ", operator='" + operator + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(int userSeq) {
        this.userSeq = userSeq;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getLeftBracket() {
        return leftBracket;
    }

    public void setLeftBracket(String leftBracket) {
        this.leftBracket = leftBracket;
    }

    public AbstractValue getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(AbstractValue leftValue) {
        this.leftValue = leftValue;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public AbstractValue getRightValue() {
        return rightValue;
    }

    public void setRightValue(AbstractValue rightValue) {
        this.rightValue = rightValue;
    }

    public String getRightBracket() {
        return rightBracket;
    }

    public void setRightBracket(String rightBracket) {
        this.rightBracket = rightBracket;
    }

    public String getLeftObjectType() {
        return leftObjectType;
    }

    public void setLeftObjectType(String leftObjectType) {
        this.leftObjectType = leftObjectType;
    }

    public String getRightObjectType() {
        return rightObjectType;
    }

    public void setRightObjectType(String rightObjectType) {
        this.rightObjectType = rightObjectType;
    }
}

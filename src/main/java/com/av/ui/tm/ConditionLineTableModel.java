package com.av.ui.tm;

import com.av.domain.AbstractValue;
import com.av.domain.ConditionLine;
import com.av.domain.ConstantValue;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexey on 19.10.15.
 */
public class ConditionLineTableModel extends AbstractTableModel {

    private List<ConditionLine> listConditionLine = new ArrayList<ConditionLine>();
    public static final int COLUMN_COUNT = 9;
    public static final int USER_SEQ = 0;
    public static final int LEFT_BRACKET = 1;
    public static final int LEFT_OBJECT_TYPE = 2;
    public static final int LEFT_OBJECT = 3;
    public static final int EXPRESSION = 4;
    public static final int RIGHT_OBJECT_TYPE = 5;
    public static final int RIGHT_OBJECT = 6;
    public static final int RIGHT_BRACKET = 7;
    public static final int OPERATOR = 8;

    public List<ConditionLine> getListConditionLine() {
        return listConditionLine;
    }

    public void setListConditionLine(List<ConditionLine> listConditionLine) {
        this.listConditionLine = listConditionLine;
    }

    public int getRowCount() {

        if (listConditionLine == null) return 0;


        return listConditionLine.size();
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        switch (columnIndex) {
            case USER_SEQ:
                return listConditionLine.get(rowIndex).getUserSeq();
            case LEFT_BRACKET:
                return listConditionLine.get(rowIndex).getLeftBracket();
            case LEFT_OBJECT_TYPE:
                return listConditionLine.get(rowIndex).getLeftObjectType();

            case LEFT_OBJECT: {
                if (listConditionLine.get(rowIndex).getLeftValue() == null) return null;
                return listConditionLine.get(rowIndex).getLeftValue().getName();
            }
            case EXPRESSION:
                return listConditionLine.get(rowIndex).getExpression();
            case RIGHT_OBJECT_TYPE:
                return listConditionLine.get(rowIndex).getRightObjectType();
            case RIGHT_OBJECT: {
                if (listConditionLine.get(rowIndex).getRightValue() == null) return null;
                return listConditionLine.get(rowIndex).getRightValue().getName();
            }
            case RIGHT_BRACKET:
                return listConditionLine.get(rowIndex).getRightBracket();
            case OPERATOR:
                return listConditionLine.get(rowIndex).getOperator();
        }


        return null;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case USER_SEQ:
                return "Приоритет";
            case LEFT_BRACKET:
                return "(";
            case LEFT_OBJECT_TYPE:
                return "Тип источника";
            case LEFT_OBJECT:
                return "Источник данных справа";
            case EXPRESSION:
                return "Выражение";
            case RIGHT_OBJECT_TYPE:
                return "Тип источника";
            case RIGHT_OBJECT:
                return "Источник данных слева";
            case RIGHT_BRACKET:
                return ")";
            case OPERATOR:
                return "И|ИЛИ";
        }
        return super.getColumnName(column);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        //  System.out.println("rowIndex="+rowIndex );
        ConditionLine line = listConditionLine.get(rowIndex);

        switch (columnIndex) {
            case USER_SEQ:
                line.setUserSeq((Integer) aValue);
                break;
            case LEFT_BRACKET:
                line.setLeftBracket((String) aValue);
                break;
            case RIGHT_BRACKET:
                line.setRightBracket((String) aValue);
                break;
            case LEFT_OBJECT_TYPE:
                line.setLeftObjectType((String) aValue);
                break;
            case LEFT_OBJECT:
                line.setLeftValue((AbstractValue) aValue);
                break;
            case EXPRESSION:
                line.setExpression((String) aValue);
                break;
            case RIGHT_OBJECT_TYPE:
                line.setRightObjectType((String) aValue);
                break;
            case RIGHT_OBJECT: {


                if (aValue == null) {
                    line.setRightValue((AbstractValue) aValue);
                    break;
                }


                if (aValue instanceof AbstractValue) line.setRightValue((AbstractValue) aValue);
                else if (aValue != null) {
                    ConstantValue v = new ConstantValue();
                    v.setCode((String) aValue);
                    v.setName((String) aValue);
                    line.setRightValue(v);

                }
            }
            break;
            case OPERATOR:
                line.setOperator((String) aValue);
                break;
        }

    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case USER_SEQ:
                return Integer.class;
            case LEFT_BRACKET:
                return String.class;
            case LEFT_OBJECT_TYPE:
                return String.class;
            case LEFT_OBJECT:
                return String.class;
            case EXPRESSION:
                return String.class;
            case RIGHT_OBJECT_TYPE:
                return String.class;
            case RIGHT_OBJECT:
                return String.class;
            case RIGHT_BRACKET:
                return String.class;
            case OPERATOR:
                return String.class;
        }
        return super.getColumnClass(columnIndex);
    }
}

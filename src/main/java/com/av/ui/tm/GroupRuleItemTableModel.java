package com.av.ui.tm;

import com.av.domain.AbstractValue;
import com.av.domain.Condition;
import com.av.domain.ConstantValue;
import com.av.domain.GroupRuleItem;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by alexey on 24.10.15.
 */
public class GroupRuleItemTableModel extends AbstractTableModel {

    private static final int COLUMN_COUNT = 4;

    public static final int PRIORITY = 0;
    public static final int VALUE_TYPE = 1;
    public static final int VALUE = 2;
    public static final int CONDITION = 3;

    private List<GroupRuleItem> groupRuleItemList;


    public int getRowCount() {
        if (groupRuleItemList == null) return 0;
        return groupRuleItemList.size();
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (groupRuleItemList == null) return null;

        switch (columnIndex) {
            case PRIORITY:
                return groupRuleItemList.get(rowIndex).getUserSeq();
            case VALUE_TYPE:
                return groupRuleItemList.get(rowIndex).getValueType();
            case VALUE:
                if (groupRuleItemList.get(rowIndex).getValue() != null) {
                    return groupRuleItemList.get(rowIndex).getValue().getName();
                } else {
                    return null;
                }
            case CONDITION: {

                JButton bt = new JButton("Условия");
                bt.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("ddddd");
                    }
                });

                return bt;
            }

        }
        return null;

    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {

        switch (columnIndex) {
            case PRIORITY:
                return Integer.class;
            case VALUE_TYPE:
                return String.class;
            case VALUE:
                return AbstractValue.class;
            case CONDITION:
                return JButton.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case PRIORITY:
                return "Приоритет";
            case VALUE_TYPE:
                return "Тип правила";
            case VALUE:
                return "Правило";
            case CONDITION:
                return "Условие";
        }
        return super.getColumnName(columnIndex);
    }

    public void setGroupRuleItemList(List<GroupRuleItem> groupRuleItemList) {
        this.groupRuleItemList = groupRuleItemList;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //  if (columnIndex == CONDITION) return false;

        return true;
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        switch (columnIndex) {
            case PRIORITY:
                groupRuleItemList.get(rowIndex).setUserSeq((Integer) aValue);
                break;
            case VALUE_TYPE:
                groupRuleItemList.get(rowIndex).setValueType((String) aValue);
                break;
            case VALUE:


                if (!(aValue instanceof AbstractValue)) {
                    ConstantValue cv = new ConstantValue();
                    cv.setCode((String) aValue);
                    cv.setName((String) aValue);
                    groupRuleItemList.get(rowIndex).setValue(cv);
                    break;
                }

                groupRuleItemList.get(rowIndex).setValue((AbstractValue) aValue);
                break;
            case CONDITION:
                groupRuleItemList.get(rowIndex).setCondition((Condition) aValue);
                break;
        }

    }

    public Condition getCondition(int rowIndex) {
        return groupRuleItemList.get(rowIndex).getCondition();
    }

}

package com.av.ui.tm;

import com.av.domain.Document;
import com.av.domain.GroupRule;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by alexey on 23.10.15.
 */
public class GroupRuleTableModel extends AbstractTableModel {


    private static final int COLUMN_COUNT = 4;
    public static final int CODE = 7;
    public static final int NAME = 0;
    public static final int DOC = 1;
    public static final int SEGMENT_NUM = 2;
    public static final int ENABLED = 3;


    private List<GroupRule> ruleGroupList;

    public void setRuleGroupList(List<GroupRule> ruleGroupList) {
        this.ruleGroupList = ruleGroupList;
    }

    public int getRowCount() {
        if (ruleGroupList == null) return 0;

        return ruleGroupList.size();
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        if (ruleGroupList == null) return null;
        switch (columnIndex) {
            case CODE:
                return ruleGroupList.get(rowIndex).getCode();
            case NAME:
                return ruleGroupList.get(rowIndex).getName();
            case DOC:
                return ruleGroupList.get(rowIndex).getDoc().getName();
            case SEGMENT_NUM:
                return ruleGroupList.get(rowIndex).getSegNum();
            case ENABLED:
                return ruleGroupList.get(rowIndex).isEnabled();

        }

        return null;


    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {

        switch (columnIndex) {
            case CODE:
                return String.class;
            case NAME:
                return String.class;
            case DOC:
                return Document.class;
            case SEGMENT_NUM:
                return Integer.class;
            case ENABLED:
                return Boolean.class;

        }

        return super.getColumnClass(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case CODE:
                return "Код правила";
            case NAME:
                return "Имя правила";
            case DOC:
                return "Документ";
            case SEGMENT_NUM:
                return "Номер сегмента для правила";
            case ENABLED:
                return "Активно";
        }


        return super.getColumnName(column);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
        // return super.isCellEditable(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        switch (columnIndex) {


            case NAME:
                ruleGroupList.get(rowIndex).setName((String) aValue);
                break;
            case DOC:
                break;
            case SEGMENT_NUM:
                ruleGroupList.get(rowIndex).setSegNum((Integer) aValue);
                break;
            case ENABLED:
                ruleGroupList.get(rowIndex).setEnabled((Boolean) aValue);
                break;
        }


    }
}

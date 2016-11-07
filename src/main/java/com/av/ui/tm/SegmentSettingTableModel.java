package com.av.ui.tm;

import com.av.domain.AbstractValue;
import com.av.domain.ConstantValue;
import com.av.domain.SegmentSetting;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alexey on 21.10.15.
 */
public class SegmentSettingTableModel extends AbstractTableModel {

    private static final int COLUMN_COUNT = 3;
    public static final int NAME = 0;
    public static final int RULE_TYPE = 1;
    public static final int RULE_CODE = 2;


    private HashMap<Integer, String> desc = new HashMap<Integer, String>();

    private List<SegmentSetting> segmentSettingsList;


    public void setList(List<SegmentSetting> list) {
        segmentSettingsList = list;
    }


    public SegmentSettingTableModel() {


        desc.put(1, "Код БК");
        desc.put(2, "Вид деятельности");
        desc.put(3, "Счет БУ");


    }

    public int getRowCount() {
        if (segmentSettingsList == null) return 0;
        return segmentSettingsList.size();
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (segmentSettingsList == null || segmentSettingsList.size() == 0) return null;

        switch (columnIndex) {
            case NAME:
                return desc.get(segmentSettingsList.get(rowIndex).getSegmentNum());
            case RULE_TYPE:
                return segmentSettingsList.get(rowIndex).getRuleType();

            case RULE_CODE:
                if (segmentSettingsList.get(rowIndex).getValue() != null) {
                    return segmentSettingsList.get(rowIndex).getValue().getName();
                }
                break;

        }

        return null;

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        if (columnIndex == 0) return false;

        return true;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case NAME:
                return "Сегмент";
            case RULE_TYPE:
                return "Тип правила";

            case RULE_CODE:
                return "Правило";

        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case RULE_TYPE:
                segmentSettingsList.get(rowIndex).setRuleType((String) aValue);
                break;
            case RULE_CODE:
                if (!(aValue instanceof AbstractValue)) {
                    ConstantValue cv = new ConstantValue();
                    cv.setCode((String) aValue);
                    cv.setName((String) aValue);
                    segmentSettingsList.get(rowIndex).setValue(cv);
                    break;
                }
                segmentSettingsList.get(rowIndex).setValue((AbstractValue) aValue);
                break;

        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case NAME:
                return String.class;
            case RULE_TYPE:
                return String.class;
            case RULE_CODE:
                return AbstractValue.class;
        }
        return super.getColumnClass(columnIndex);
    }
}

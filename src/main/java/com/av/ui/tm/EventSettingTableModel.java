package com.av.ui.tm;

import com.av.domain.EventSetting;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by vasil on 19.10.2015.
 */
public class EventSettingTableModel extends AbstractTableModel {
    private static final int COLUMN_COUNT = 2;
    public static final int CODE = 0;
    public static final int ENABLED = 1;

    List<EventSetting> list;


    public EventSetting getByRowIndex(int rowIndex) {
        if (list.size() < 0) return null;
        return list.get(rowIndex);
    }

    public void setList(List<EventSetting> list) {
        this.list = list;
    }


    public int getRowCount() {
        if (list == null) return 0;
        return list.size();
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case CODE:
                return String.class;
            case ENABLED:
                return Boolean.class;

        }
        return super.getColumnClass(columnIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        switch (columnIndex) {
            case CODE:
                return list.get(rowIndex).getCode();
            case ENABLED:
                return list.get(rowIndex).isEnabled();

        }
        return null;
    }


    @Override
    public String getColumnName(int column) {
        switch (column) {
            case CODE:
                return "Код настройки";
            case ENABLED:
                return "Активно";

        }

        return super.getColumnName(column);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


        switch (columnIndex) {
            case CODE:
                list.get(rowIndex).setCode((String) aValue);
                break;
            case ENABLED:
                list.get(rowIndex).setEnabled((Boolean) aValue);
                break;
        }


    }
}

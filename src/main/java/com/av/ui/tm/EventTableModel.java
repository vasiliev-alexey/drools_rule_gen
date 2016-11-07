package com.av.ui.tm;

import com.av.domain.Document;
import com.av.domain.Event;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexey on 18.10.15.
 */
public class EventTableModel extends AbstractTableModel {
    private static final int COLUMN_COUNT = 4;
    public static final int CODE = 0;
    public static final int NAME = 1;
    public static final int DOC = 2;
    public static final int ENABLED = 3;

    private List<Event> eventList = new ArrayList<Event>();

    public EventTableModel(List<Event> eventList) {
        this.eventList = eventList;
    }

    public int getRowCount() {
        if (eventList == null) return 0;
        return eventList.size();
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        if (eventList.size() == 0) return null;

        switch (columnIndex) {
            case CODE:
                return eventList.get(rowIndex).getCode();
            case NAME:
                return eventList.get(rowIndex).getName();

            case DOC:
                if (eventList.get(rowIndex).getDoc() != null) {
                    return eventList.get(rowIndex).getDoc().getName();
                }
                break;
            case ENABLED:
                return eventList.get(rowIndex).isEnabled();
        }

        return null;

    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case CODE:
                return "Код события";
            case NAME:
                return "Наименование события";
            case DOC:
                return "Документ для события";
            case ENABLED:
                return "Активно";
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
                return String.class;
            case ENABLED:
                return Boolean.class;
        }

        return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        Event e = eventList.get(rowIndex);

        if (columnIndex == ENABLED) return true;
        return e.getId() < 1;


    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        Event e = eventList.get(rowIndex);

        switch (columnIndex) {
            case CODE:
                e.setCode((String) aValue);
                break;
            case NAME:
                e.setName((String) aValue);
                break;
            case DOC:
                e.setDoc((Document) aValue);
                break;
            case ENABLED:
                e.setEnabled((Boolean) aValue);
                break;
        }


    }
}

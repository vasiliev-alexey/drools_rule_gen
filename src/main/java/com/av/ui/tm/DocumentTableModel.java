package com.av.ui.tm;

import com.av.domain.Document;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by alexey on 18.10.15.
 */
public class DocumentTableModel extends AbstractTableModel {

    private static final int COLUMN_COUNT = 3;
    private static final int CODE = 0;
    private static final int NAME = 1;


    private List<Document> listDoc;

    public void setList(List<Document> listDoc) {

        this.listDoc = listDoc;

    }

    ;

    public int getRowCount() {
        return listDoc.size();
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        if (listDoc.size() == 0) return null;

        switch (columnIndex) {
            case CODE:
                return listDoc.get(rowIndex).getCode();
            case NAME:
                return listDoc.get(rowIndex).getName();
        }

        return null;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case CODE:
                return "Код документа";
            case NAME:
                return "Наименование документа";
        }

        return null;
    }
}

package com.av.ui.frames;

import com.av.dao.DocumentDAO;
import com.av.domain.Document;
import com.av.ui.tm.DocumentAttributeTableModel;
import com.av.ui.tm.DocumentTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
//import java.awt.*;

/**
 * Created by alexey on 16.10.15.
 */
@Component
@Lazy
public class DocumentFrame extends JFrame {

    @Autowired
    private DocumentDAO documentDAO;

    @PostConstruct
    public void init() {

        setMinimumSize(new Dimension(1000, 700));
        setTitle("Структуры документов");
        // тут будут структуры документов


        JSplitPane splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.25d);
        add(splitPane);
        JPanel topPanel = new JPanel();
        splitPane.setTopComponent(topPanel);
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder
                (BorderFactory.createLineBorder(Color.blue), "Документ"));

        DocumentTableModel topModel = new DocumentTableModel();
        final JTable topTable = new JTable(topModel);
        topPanel.add(new JScrollPane(topTable));
        final List<Document> topList = documentDAO.findAll();
        topModel.setList(topList);


        JPanel bottomPanel = new JPanel();
        splitPane.setBottomComponent(bottomPanel);

        final DocumentAttributeTableModel bottomModel = new DocumentAttributeTableModel();
        final JTable bottomTable = new JTable(bottomModel);
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(new JScrollPane(bottomTable));


        topTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int r = topTable.getSelectedRow();
                int tr = topTable.convertRowIndexToModel(r);
                Document doc = topList.get(tr);

                if (doc != null) {
                    bottomModel.setList(doc.getDocumentAttributeList());
                    bottomModel.fireTableDataChanged();
                }

            }
        });


    }

}

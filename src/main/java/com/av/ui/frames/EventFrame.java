package com.av.ui.frames;

import com.av.dao.DocumentDAO;
import com.av.dao.EventDAO;
import com.av.domain.Document;
import com.av.domain.Event;
import com.av.ui.tm.EventTableModel;
import com.av.ui.tm.renders.DocumentRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


/**
 * Created by alexey on 16.10.15.
 */
@Component
public class EventFrame extends JFrame {
    @Autowired
    private EventDAO eventDAO;
    @Autowired
    private DocumentDAO documentDao;


    @PostConstruct
    public void init() {

        setMinimumSize(new Dimension(1000, 700));
        setTitle("События по документам");
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        add(panel);
        panel.setLayout(new BorderLayout());
        final List<Event> list = eventDAO.findAll();
        final EventTableModel model = new EventTableModel(list);
        JTable table = new JTable(model);


        JComboBox<Document> docCombo = new JComboBox<Document>();
        List<Document> listDoc = documentDao.findAll();
        docCombo.setRenderer(new DocumentRender());
        for (Document d : listDoc) {
            docCombo.addItem(d);
        }
        DefaultCellEditor editor = new DefaultCellEditor(docCombo);

        table.getColumnModel().getColumn(model.DOC).setCellEditor(editor);


        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel mngPanel = new JPanel();
        add(mngPanel, BorderLayout.NORTH);
        JButton btAdd = new JButton("Добавить событие");
        mngPanel.add(btAdd);
        btAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                list.add(new Event());
                model.fireTableDataChanged();
            }
        });

        JButton btSave = new JButton("Сохранить");
        mngPanel.add(btSave);
        btSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eventDAO.saveList(list);
            }
        });

        // тут будут структуры документов


    }
}

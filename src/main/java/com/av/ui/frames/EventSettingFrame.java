package com.av.ui.frames;

import com.av.dao.CustomRuleDao;
import com.av.dao.EventDAO;
import com.av.dao.GroupRuleDAO;
import com.av.domain.*;
import com.av.domain.Event;
import com.av.ui.tables.SegmentTable;
import com.av.ui.tm.EventSettingTableModel;
import com.av.ui.tm.SegmentSettingTableModel;
import com.av.ui.tm.renders.AbstractValueRender;
import com.av.ui.tm.renders.DocumentRender;
import com.av.ui.tm.renders.EventRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alexey on 18.10.15.
 */
@Component
public class EventSettingFrame extends JFrame {

    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private ConditionFrame conditionFrame;
    @Autowired
    private CustomRuleDao customRuleDao;
    @Autowired
    private GroupRuleDAO groupRuleDAO;

    @Autowired
    private GroupRuleFrame groupRuleFrame;

    private Event event = new Event();

    private EventSettingTableModel esTM;

    private JTable sttTable;
    private EventSettingTableModel tbModel = new EventSettingTableModel();


    private SegmentSettingTableModel ssDrModel = new SegmentSettingTableModel();
    private SegmentSettingTableModel ssCrModel = new SegmentSettingTableModel();
    private SegmentTable drTable = new SegmentTable(ssDrModel);
    private SegmentTable crTable = new SegmentTable(ssCrModel);
    private JComboBox<DocumentAttribute> attrDocCombo = new JComboBox<DocumentAttribute>();
    private JComboBox<GroupRule> groupRuleCombo = new JComboBox<GroupRule>();

    private JComboBox<Object> comboDoc;

    private boolean supressEvent = true;

    private JComboBox<CustomRule> ruleCodesCombo = new JComboBox<CustomRule>();

    public void setEvent(Event e) {
        this.event = e;
    }


    @PostConstruct
    public void init() {

        setSize(new Dimension(1000, 700));
        setLayout(new BorderLayout());
        add(getInfoPanel(), BorderLayout.NORTH);

        JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        add(jSplitPane, BorderLayout.CENTER);
        jSplitPane.setResizeWeight(0.10d);
        jSplitPane.setTopComponent(getSettingPanel());
        jSplitPane.setBottomComponent(getSegementPanel());

        HashMap<String, DefaultCellEditor> editors = new HashMap<String, DefaultCellEditor>();
        editors.put("Источник", new DefaultCellEditor(ruleCodesCombo));
        editors.put("Атрибут документа", new DefaultCellEditor(attrDocCombo));
        editors.put("Константа", new DefaultCellEditor(new JTextField()));
        editors.put("Группа правил", new DefaultCellEditor(groupRuleCombo));

        drTable.setEditors(editors);
        crTable.setEditors(editors);

        ruleCodesCombo.setRenderer(new AbstractValueRender());
        attrDocCombo.setRenderer(new AbstractValueRender());
        groupRuleCombo.setRenderer(new AbstractValueRender());


    }


    private JPanel getInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder("Информация по документу"));
        final List<Event> listEvent = eventDAO.findEnable();

        comboDoc = new JComboBox<Object>();
        final JComboBox<Event> comboEvent = new JComboBox<Event>();

        HashMap<Long, Document> hmdoc = new HashMap<Long, Document>();

        for (Event ev : listEvent) {
            hmdoc.put(ev.getDoc().getId(), ev.getDoc());
            // comboEvent.addItem(ev);
        }
        // comboDoc.addItem(null);
        for (Document d : hmdoc.values()) {
            comboDoc.addItem(d);
        }
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));

        final EventRender eventRender = new EventRender();
        comboEvent.setRenderer(eventRender);
        DocumentRender docRender = new DocumentRender();
        comboDoc.setRenderer(docRender);
        // comboDoc.repaint();
        infoPanel.add(comboDoc);
        infoPanel.add(comboEvent);


        comboDoc.setSelectedItem(null);


        comboDoc.addItemListener(new ItemListener() {
                                     public void itemStateChanged(ItemEvent e) {
                                         Document item = (Document) e.getItem();
                                         // comboEvent.removeAll();
                                         comboEvent.removeAllItems();


                                         supressEvent = false;
                                         for (Event ev : listEvent) {
                                             if (ev.getDoc().getId() == item.getId())
                                                 comboEvent.addItem(ev);
                                         }
                                         comboEvent.setSelectedIndex(-1);
                                         comboEvent.setSelectedItem(null);
                                         event = null;
                                         supressEvent = true;

                                         attrDocCombo.removeAllItems();
                                         for (DocumentAttribute docAtr : item.getDocumentAttributeList()) {
                                             attrDocCombo.addItem(docAtr);
                                         }

                                         ruleCodesCombo.removeAllItems();
                                         for (CustomRule cr : customRuleDao.findAllByDoc(item.getId())) {
                                             ruleCodesCombo.addItem(cr);
                                         }

                                         groupRuleCombo.removeAllItems();
                                         for (GroupRule gr : groupRuleDAO.findAllEnabledByDocId(item.getId())) {
                                             groupRuleCombo.addItem(gr);
                                         }


                                         resetPanel();

                                     }
                                 }
        );


        comboEvent.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {

                //System.out.println("dddd+"  + e.getStateChange());

                if (supressEvent) {
                    event = (Event) e.getItem();
                    tbModel.setList(event.getEventSettingList());
                    tbModel.fireTableDataChanged();

                    resetPanel();
                }
            }
        });
        infoPanel.validate();
        return infoPanel;
    }

    private JPanel getSettingPanel() {

        JPanel topPanel = new JPanel();
        topPanel.setMinimumSize(new Dimension(getWidth(), 150));
        topPanel.setBorder(BorderFactory.createTitledBorder("Настройка строк событий"));


        JPanel mngPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(mngPanel, BorderLayout.SOUTH);

        sttTable = new JTable(tbModel);


        topPanel.add(new JScrollPane(sttTable), BorderLayout.CENTER);

        final JButton btCondition = new JButton("Настройка условий");
        btCondition.setEnabled(false);
        mngPanel.add(btCondition);
        btCondition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                conditionFrame.setDoc(event.getDoc());
                int r = sttTable.getSelectedRow();
                int t = sttTable.convertRowIndexToModel(r);

                EventSetting es = tbModel.getByRowIndex(t);
                // conditionFrame.setSetting(es);
                if (es.getCondition() == null) es.setCondition(new Condition());
                conditionFrame.setCondition(es.getCondition());
                conditionFrame.afterSetProp();
                conditionFrame.setVisible(true);
            }
        });
        sttTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int r = sttTable.getSelectedRow();
                int t = sttTable.convertRowIndexToModel(r);
                EventSetting es = tbModel.getByRowIndex(t);

                List<SegmentSetting> drList = new ArrayList<SegmentSetting>();
                List<SegmentSetting> crList = new ArrayList<SegmentSetting>();

                for (SegmentSetting s : es.getSegmentSettingList()) {

                    if (s.getTypeEntry().equals("D")) {
                        drList.add(s);
                    } else {
                        crList.add(s);
                    }

                }


                ssDrModel.setList(drList);
                ssCrModel.setList(crList);
                ssDrModel.fireTableDataChanged();
                ssCrModel.fireTableDataChanged();

                btCondition.setEnabled(true);
            }
        });

        JButton btAddES = new JButton("Добавить настройку");
        mngPanel.add(btAddES);
        JButton btSaveEvent = new JButton("Сохранить");
        mngPanel.add(btSaveEvent);


        btAddES.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                List<EventSetting> list = event.getEventSettingList();
                list.add(new EventSetting());
                tbModel.fireTableDataChanged();

            }
        });

        btSaveEvent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Event> list = new ArrayList<Event>();
                list.add(event);
                eventDAO.saveList(list);
            }
        });

        JButton btGroupRule = new JButton("Настройка групп правил для документа");
        mngPanel.add(btGroupRule);
        btGroupRule.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Document doc = (Document) comboDoc.getSelectedItem();
                if (doc == null) return;

                groupRuleFrame.showRuleGroupForm(doc);
                groupRuleFrame.setVisible(true);

            }
        });


        return topPanel;
    }

    private JPanel getSegementPanel() {

        JPanel botomPanel = new JPanel();
        botomPanel.setBorder(BorderFactory.createTitledBorder("Настройка сегментов"));
        botomPanel.setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5d);
        botomPanel.add(splitPane, BorderLayout.CENTER);

        JPanel drPanel = new JPanel();
        JPanel crPanel = new JPanel();

        drPanel.setBorder(BorderFactory.createTitledBorder("Дебет"));
        crPanel.setBorder(BorderFactory.createTitledBorder("Кредит"));
        drPanel.setLayout(new BorderLayout());
        crPanel.setLayout(new BorderLayout());
        splitPane.setLeftComponent(drPanel);
        splitPane.setRightComponent(crPanel);

        botomPanel.setPreferredSize(new Dimension(getWidth(), 500));


        drPanel.add(new JScrollPane(drTable), BorderLayout.CENTER);
        crPanel.add(new JScrollPane(crTable), BorderLayout.CENTER);


        return botomPanel;
    }

    private void resetPanel() {
        ssDrModel.setList(null);
        ssCrModel.setList(null);
        ssDrModel.fireTableDataChanged();
        ssCrModel.fireTableDataChanged();
    }

}

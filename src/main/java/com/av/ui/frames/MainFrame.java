package com.av.ui.frames;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by alexey on 16.10.15.
 */
public class MainFrame extends JFrame {

    @Autowired
    private DocumentFrame documentFrame;
    @Autowired
    private EventFrame eventFrame;
    @Autowired
    private EventSettingFrame eventSettingFrame;

    @PostConstruct
    public void init() {

        setTitle("Главная страница");
        setMinimumSize(new Dimension(1000, 700));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.blue), "Меню"));
        topPanel.add(addDocBtn());
        topPanel.add(addEventBtn());
        topPanel.add(addEventSttBtn());
        topPanel.add(addUnloadBtn());

        add(topPanel);


        setVisible(true);
    }


    private JButton addDocBtn() {

        JButton btn = new JButton("Структура документа");

        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                documentFrame.setVisible(true);
            }
        });

        return btn;
    }

    private JButton addEventBtn() {

        JButton btn = new JButton("События");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eventFrame.setVisible(true);
            }
        });


        return btn;
    }

    private JButton addEventSttBtn() {

        JButton btn = new JButton("Настройка учета для события");

        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eventSettingFrame.setVisible(true);
            }
        });

        return btn;
    }

    private JButton addUnloadBtn() {

        JButton btn = new JButton("Выгрузка учета");

        return btn;
    }
}

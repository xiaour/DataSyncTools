package io.github.xiaour.datasync.ui.panel;

import io.github.xiaour.datasync.App;
import io.github.xiaour.datasync.ui.UiConsts;
import io.github.xiaour.datasync.ui.component.MyIconButton;
import io.github.xiaour.datasync.tools.PropertyUtil;

import javax.swing.*;
import java.awt.*;

/**
 * 工具栏面板
 *
 * @author Bob
 */
public class ToolBarPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static MyIconButton buttonDatabase;
    private static MyIconButton buttonSetting;

    /**
     * 构造
     */
    public ToolBarPanel() {
        initialize();
        addButtion();
        addListener();
    }

    /**
     * 初始化
     */
    private void initialize() {
        Dimension preferredSize = new Dimension(48, UiConsts.MAIN_WINDOW_HEIGHT);
        this.setPreferredSize(preferredSize);
        this.setMaximumSize(preferredSize);
        this.setMinimumSize(preferredSize);
        this.setBackground(UiConsts.TOOL_BAR_BACK_COLOR);
        this.setLayout(new GridLayout(2, 1));
    }

    /**
     * 添加工具按钮
     */
    private void addButtion() {

        JPanel panelUp = new JPanel();
        panelUp.setBackground(UiConsts.TOOL_BAR_BACK_COLOR);
        panelUp.setLayout(new FlowLayout(-2, -2, -4));

        buttonDatabase = new MyIconButton(UiConsts.ICON_DATABASE, UiConsts.ICON_DATABASE_ENABLE,UiConsts.ICON_DATABASE, PropertyUtil.getProperty("ds.ui.status.title"));
        buttonSetting  = new MyIconButton(UiConsts.ICON_SETTING, UiConsts.ICON_SETTING_ENABLE, UiConsts.ICON_SETTING, PropertyUtil.getProperty("ds.ui.setting.title"));

        panelUp.add(buttonDatabase);
        panelUp.add(buttonSetting);

        this.add(panelUp);

    }

    /**
     * 为各按钮添加事件动作监听
     */
    private void addListener() {

        buttonDatabase.addActionListener(e -> {

            buttonDatabase.setIcon(UiConsts.ICON_DATABASE_ENABLE);
            buttonSetting.setIcon(UiConsts.ICON_SETTING);

            App.mainPanelCenter.removeAll();
            DatabasePanelFrom.setContent();
            DatabasePanelTo.setContent();
            App.mainPanelCenter.add(App.databasePanel, BorderLayout.CENTER);

            App.mainPanelCenter.updateUI();

        });

        buttonSetting.addActionListener(e -> {

            buttonDatabase.setIcon(UiConsts.ICON_DATABASE);
            buttonSetting.setIcon(UiConsts.ICON_SETTING_ENABLE);

            App.mainPanelCenter.removeAll();
            DatabasePanelFrom.setContent();
            DatabasePanelTo.setContent();
            App.mainPanelCenter.add(App.settingPanel, BorderLayout.CENTER);

            App.mainPanelCenter.updateUI();

        });
    }
}

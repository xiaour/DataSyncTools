package io.github.xiaour.datasync.logic.init;

import com.opencsv.CSVWriter;
import io.github.xiaour.datasync.tools.DbUtilMySQL;
import io.github.xiaour.datasync.tools.DbUtilSQLServer;
import io.github.xiaour.datasync.tools.LogLevel;
import io.github.xiaour.datasync.tools.StatusLog;
import io.github.xiaour.datasync.ui.panel.StatusPanel;

import java.io.File;

public class Init4pxp2p {

    public static boolean init() {
        StatusLog.setStatusDetail("开始初始化第一次快照，请耐心等待……", LogLevel.INFO);

        boolean isSuccess = true;
        DbUtilMySQL mySql = DbUtilMySQL.getInstance();
        DbUtilSQLServer sqlserver = DbUtilSQLServer.getInstance();
        CSVWriter csvWriterRole = null;
        CSVWriter csvWriterUser = null;
        File snapsDir = null;
        StatusPanel.progressCurrent.setMaximum(7);
        int progressValue = 0;
        StatusPanel.progressCurrent.setValue(progressValue);

        /*Do Sth you need to init*/
        return isSuccess;
    }

}

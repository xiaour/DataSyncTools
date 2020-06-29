package io.github.xiaour.datasync.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import io.github.xiaour.datasync.logic.sync.DataBaseInfo;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置文件管理 单例
 *
 * @author Bob
 */
public class ConfigManager {
    private volatile static ConfigManager confManager;
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    public Document document;

    /**
     * 私有的构造
     */
    private ConfigManager() {
        reloadDom();
    }

    /**
     * 读取xml，加载到dom
     */
    public void reloadDom() {
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(new File(ConstantsTools.PATH_CONFIG));
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.error("Read config xml error:" + e.toString());
        }
    }

    /**
     * 获取实例，线程安全
     *
     * @return
     */
    public static ConfigManager getConfigManager() {
        if (confManager == null) {
            synchronized (ConfigManager.class) {
                if (confManager == null) {
                    confManager = new ConfigManager();
                }
            }
        }
        return confManager;
    }

    /**
     * 把document对象写入新的文件
     *
     * @throws Exception
     */
    public void writeToXml() throws Exception {
        // 排版缩进的格式
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 设置编码
        format.setEncoding("UTF-8");
        // 创建XMLWriter对象,指定了写出文件及编码格式
        XMLWriter writer = null;
        writer = new XMLWriter(
                new OutputStreamWriter(new FileOutputStream(new File(ConstantsTools.PATH_CONFIG)), StandardCharsets.UTF_8), format);

        // 写入
        writer.write(document);
        writer.flush();
        writer.close();

    }

    public String getLastSyncTime() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_LAST_SYNC_TIME).getText();
    }

    public void setLastSyncTime(String lastSyncTime) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_LAST_SYNC_TIME).setText(lastSyncTime);
        writeToXml();
    }

    public String getLastKeepTime() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_LAST_KEEP_TIME).getText();
    }

    public void setLastKeepTime(String lastKeepTime) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_LAST_KEEP_TIME).setText(lastKeepTime);
        writeToXml();
    }

    public String getSuccessTime() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_SUCCESS_TIME).getText();
    }

    public void setSuccessTime(String successTime) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_SUCCESS_TIME).setText(successTime);
        writeToXml();
    }

    public String getFailTime() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_FAIL_TIME).getText();
    }

    public void setFailTime(String failTime) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_FAIL_TIME).setText(failTime);
        writeToXml();
    }

    public String getTypeFrom() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_TYPE_FROM).getText();
    }

    public void setTypeFrom(String typeFrom) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_TYPE_FROM).setText(typeFrom);
        writeToXml();
    }

    public String getHostFrom() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_HOST_FROM).getText();
    }

    public void setHostFrom(String hostFrom) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_HOST_FROM).setText(hostFrom);
        writeToXml();
    }

    public String getNameFrom() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_NAME_FROM).getText();
    }

    public void setNameFrom(String nameFrom) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_NAME_FROM).setText(nameFrom);
        writeToXml();
    }

    public void setTablesFrom(String tablesFrom) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_TABLES_FROM).setText(tablesFrom);
        writeToXml();
    }

    public String getUserFrom() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_USER_FROM).getText();
    }

    public void setUserFrom(String userFrom) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_USER_FROM).setText(userFrom);
        writeToXml();
    }

    public String getPasswordFrom() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_PASSWORD_FROM).getText();
    }

    public void setPasswordFrom(String passwordFrom) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_PASSWORD_FROM).setText(passwordFrom);
        writeToXml();
    }

    public String getTypeTo() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_TYPE_TO).getText();
    }

    public void setTypeTo(String typeTo) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_TYPE_TO).setText(typeTo);
        writeToXml();
    }

    public String getHostTo() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_HOST_TO).getText();
    }

    public void setHostTo(String hostTo) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_HOST_TO).setText(hostTo);
        writeToXml();
    }

    public String getNameTo() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_NAME_TO).getText();
    }

    public void setNameTo(String nameTo) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_NAME_TO).setText(nameTo);
        writeToXml();
    }

    public String getUserTo() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_USER_TO).getText();
    }

    public void setUserTo(String userTo) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_USER_TO).setText(userTo);
        writeToXml();
    }

    public String getPasswordTo() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_PASSWORD_TO).getText();
    }

    public void setPasswordTo(String passwordTo) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_PASSWORD_TO).setText(passwordTo);
        writeToXml();
    }

    public String getTablesFrom() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_TABLES_FROM).getText();
    }

    /**
     * 获取源数据库链接信息
     * @return
     */
    public DataBaseInfo getDataBaseFrom(){
        DataBaseInfo base = new DataBaseInfo();
        base.setHost(getHostFrom());
        base.setName(getNameFrom());
        base.setPassword(getPasswordFrom());
        base.setUser(getUserFrom());
        Set<String> stringSet = new HashSet<>();
        String tables = getTablesFrom();
        for(String table:tables.split(",")){
            stringSet.add(table);
        }
        base.setTables(stringSet);
        return base;
    }

    /**
     * 获取目标数据库链接信息
     * @return
     */
    public DataBaseInfo getDataBaseTo(){
        DataBaseInfo base = new DataBaseInfo();
        base.setHost(getHostTo());
        base.setName(getNameTo());
        base.setPassword(getPasswordTo());
        base.setUser(getUserTo());
        return base;
    }

    public String getSchedule() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_SCHEDULE).getText();
    }

    public void setSchedule(String schedule) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_SCHEDULE).setText(schedule);
        writeToXml();
    }

    public String getScheduleFixTime() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_SCHEDULE_FIX_TIME).getText();
    }

    public void setScheduleFixTime(String fixTime) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_SCHEDULE_FIX_TIME).setText(fixTime);
        writeToXml();
    }

    public String getAutoBak() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_AUTO_BAK).getText();
    }

    public void setAutoBak(String autoBak) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_AUTO_BAK).setText(autoBak);
        writeToXml();
    }

    public String getDebugMode() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_DEBUG_MODE).getText();
    }

    public void setDebugMode(String debugMode) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_DEBUG_MODE).setText(debugMode);
        writeToXml();
    }

    public String getStrictMode() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_STRICT_MODE).getText();
    }

    public void setStrictMode(String strictMode) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_STRICT_MODE).setText(strictMode);
        writeToXml();
    }

    public String getMysqlPath() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_MYSQL_PATH).getText();
    }

    public void setMysqlPath(String mysqlPath) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_MYSQL_PATH).setText(mysqlPath);
        writeToXml();
    }

    public String getProductName() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_PRODUCT_NAME).getText();
    }

    public void setProductName(String productName) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_PRODUCT_NAME).setText(productName);
        writeToXml();
    }

    public String getPositionCode() {
        return this.document.selectSingleNode(ConstantsTools.XPATH_POSITION_CODE).getText();
    }

    public void setPositionCode(String positionCode) throws Exception {
        this.document.selectSingleNode(ConstantsTools.XPATH_POSITION_CODE).setText(positionCode);
        writeToXml();
    }
}

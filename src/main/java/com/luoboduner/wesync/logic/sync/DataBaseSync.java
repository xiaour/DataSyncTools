package com.luoboduner.wesync.logic.sync;

import com.google.common.base.Joiner;
import com.luoboduner.wesync.enums.DangerOperationEnum;
import com.luoboduner.wesync.tools.DbUtilMySQL;
import com.luoboduner.wesync.uitls.SqlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhangtao
 * @Class DataBaseSync
 * @Description
 * @Date 2020/6/28 15:55
 * @Version 1.0.0
 */
public class DataBaseSync {


    /*
     * 获取一个表的列名，用于 readData 时指定一个表的字段，之所以不使用 * 是因为源数据库表的字段有可能新增，
     * 也可以用于表结构的自动同步：当目标表的字段数少于源表时，对目标表自动执行 alter table add columns xxx
     */


    private static final Logger logger = LoggerFactory.getLogger(DataBaseSync.class);
    private  Integer bufferRows =100;
    private Connection dbConnFrom;
    private Connection dbConnTo;
    private DataBaseInfo dbFrom;
    private DataBaseInfo dbTo;
    private Integer dangerOpt;

    public int currentProgress;

    public enum ColSizeTimes{

        EQUAL(1),DOUBLE(2);
        private int times = 0;
        // 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
        private ColSizeTimes(int times) {
            this.times = times;
        }
        public int getTimes(){
            return times;
        }
    }


    public DataBaseSync(DataBaseInfo from, DataBaseInfo to, final Integer dangerOpt,Integer currentProgress, final Integer bufferRows) throws SQLException, ClassNotFoundException {

        DbUtilMySQL dbUtilMySQL = DbUtilMySQL.getInstance();
        this.dbConnFrom = dbUtilMySQL.getConnection(from);
        this.dbConnTo = dbUtilMySQL.getConnection(to);
        this.bufferRows = bufferRows;
        this.dbFrom = from;
        this.dbTo = to;
        this.dangerOpt = dangerOpt;
        this.currentProgress = currentProgress;
        logger.info( "from "+from.getHost()+ " connection establied.");
        logger.info( "to "+to.getHost()+ " connection establied.");
    }

    public List<String> syncDataBase() throws SQLException, IOException {

        List<String> errorMsgList = this.existsTables();
        currentProgress+=5;        // 原表必须存在
        if (errorMsgList.size() > 0) {
            return errorMsgList;
        }
        //同步数据
        errorMsgList.addAll(this.writeData());

        String successMsg = "OK,已经全部完成！";

        errorMsgList.add(successMsg);

        return  errorMsgList;

    }



    public List<Integer> getColumnTypes(String schemaName,String tableName) throws SQLException {
        List<Integer> colTypes = new ArrayList<>();
        String tableCatalog = schemaName;

        DatabaseMetaData metaData = this.dbConnFrom.getMetaData();
        ResultSet resultSet = metaData.getColumns(tableCatalog, schemaName, tableName, "%");
        while (resultSet.next()) {
            colTypes.add(resultSet.getInt(5));
        }

        currentProgress+=5;

        return colTypes;

    }


    /* 判断全部表是否存在 */
    public List<String> existsTables() throws SQLException {

        List<String> msgList = new ArrayList<>();

        DatabaseMetaData metaData = this.dbConnFrom.getMetaData();

        for(String tableName:dbFrom.getTables()) {
            ResultSet resultSet = metaData.getTables(null, dbFrom.getName(), tableName, null);
            if (resultSet.next()) {
                logger.info("succes");
            }else {
                msgList.add("错误:" + tableName + " 不存在!");
            }
        }
        return msgList;
    }

    /* 判断一个表是否存在 */
    public boolean existsTable(Connection connection,String dbName,String tableName) throws SQLException {

        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet resultSet = metaData.getTables(null,dbName, tableName, null);

        if (resultSet.next()) {
            return  true;
        }

        return false;
    }

    /* 获取一个表的 DDL 语句，用于在目标数据库创建表 lenSize 默认值 1 */
    public String getDDL( String tableName, ColSizeTimes lenSize) throws SQLException {

        String showDDLSql="show create table " + tableName;

        String resultDDL ="";

        PreparedStatement pStemt = this.dbConnFrom.prepareStatement(showDDLSql);

        ResultSet resultSet = pStemt.executeQuery();

        if (resultSet.next()) {
            resultDDL = resultSet.getString("Create Table");
        }

        logger.info(String.format("get ddl from %s ", tableName));

        currentProgress+=5;

        return resultDDL;

    }

    /* 获取一个表的数据，采用流式读取，可以提供 whereClause 表示增量读取 ，如果 whereClause 为空，表示全量读取 */
    public ResultSet readData(final String tableName, final List<String> columnNames,
                              String whereClause) throws SQLException {
        this.dbConnFrom.setAutoCommit(false);
        String schemaName =  this.dbFrom.getName();
        logger.info(String.format("read data from  %s.%s ", schemaName, tableName));
        final String columns = Joiner.on(", ").join(columnNames);
        String selectSql = "";
        if (whereClause == null || whereClause.isEmpty()) {
            selectSql = "select " + columns + " from " + schemaName + "." + tableName;
        } else {
            selectSql = "select " + columns + " from " + schemaName + "." + tableName + " where " + whereClause;
        }
        logger.info(selectSql);
        PreparedStatement pStemt = null;
        pStemt = this.dbConnFrom.prepareStatement(selectSql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        pStemt.setFetchSize(bufferRows + 10000);
        final ResultSet rs = pStemt.executeQuery();
        return rs;

    }

    public List<String> getTableColumns(String schemaName, String tableName) throws SQLException {
        logger.info(String.format("get table columns from %s.%s ", schemaName, tableName));
        List<String> columnNames = new ArrayList<>();
        DatabaseMetaData metaData = this.dbConnFrom.getMetaData();

        String tableCatalog = schemaName;

        ResultSet resultSet = metaData.getColumns(tableCatalog, schemaName, tableName, "%");
        while (resultSet.next()) {
            columnNames.add(resultSet.getString(4));
        }
        return columnNames;

    };

    /* 将 ResultSet 类型的数据定入一张表，写入成功返回 true,否则返回 false */
    public List<String> writeData() throws IOException, SQLException {
        List<String> msgList = new ArrayList<>();

        this.dbConnTo.setAutoCommit(false);

        String schemaName =  dbTo.getName();
        for(String tableName: dbFrom.getTables()){
            String ddl = this.getDDL(tableName,ColSizeTimes.EQUAL);

            this.dropAndCreateTable(tableName,ddl);

            //数据表的列
            List<String> columnNames = getTableColumns(dbFrom.getName(),tableName);

            ResultSet rs = this.readData(tableName, columnNames, "");

            logger.info(String.format("clear data for %s is done", tableName));

            List<Integer> colTypes = this.getColumnTypes(dbFrom.getName(),tableName);


            final String insertSql = SqlBuilder.buildInsertSql(tableName, columnNames.stream().toArray(String[]::new));
            PreparedStatement pStemt = this.dbConnTo.prepareStatement(insertSql);

            final int numberOfCols = columnNames.size();
            int rowCount = 0;
            long totalAffectRows = 0;
            long starttime = System.currentTimeMillis();
            while (rs.next()) {
                for (int i = 0; i < numberOfCols; i++) {
                    if (colTypes.get(i) == Types.VARCHAR || colTypes.get(i) == Types.CHAR || colTypes.get(i) == Types.CLOB) {
                        pStemt.setString(i + 1, Objects.toString(rs.getString(i + 1), "")); // 将 null 转化为空
                    } else {
                        pStemt.setObject(i + 1, rs.getObject(i + 1));
                    }
                }
                pStemt.addBatch();
                rowCount++;
                if (rowCount >= bufferRows) {
                    // 每10万行提交一次记录
                    int affectRows = 0;
                    for (int i : pStemt.executeBatch()) {
                        affectRows += i;
                    }
                    this.dbConnTo.commit();
                    logger.info(String.format("rows insert into %s is %d", tableName, affectRows));
                    totalAffectRows += affectRows;
                    rowCount = 0;
                }

            }
            currentProgress+=60;
            // 处理剩余的记录
            int affectRows = 0;
            for (int i : pStemt.executeBatch()) {
                affectRows += i;
            }
            this.dbConnTo.commit();
            logger.info(String.format("rows insert into %s is %d", tableName, affectRows));
            totalAffectRows += affectRows;
            long endtime = System.currentTimeMillis();
            String msg = String.format("insert into %s %d rows is done. cost %.3f seconds", tableName,totalAffectRows,(endtime - starttime) * 1.0 / 1000);
            logger.info(msg);
            msgList.add(msg);
            currentProgress=100;
        }
       return msgList;
    };

    /**
     * 根据条件判断是否删除或创建表
     * @param tableName
     * @param ddl
     * @return
     * @throws SQLException
     */
    public boolean dropAndCreateTable(String tableName, String ddl) throws SQLException {
        if(dangerOpt.equals(DangerOperationEnum.FULL_DATA_AND_TABLE.ordinal())) {
            if(this.existsTable(dbConnTo,dbTo.getName(),tableName)) {
                String dropTableSql = "DROP TABLE " + dbTo.getName() + "." + tableName;
                logger.info(String.format("drop table for %s is begin...", tableName));
                PreparedStatement pStemt = this.dbConnTo.prepareStatement(dropTableSql);
                pStemt.execute();
            }
            //创建表
            String[] sqls = ddl.split(";");
            for (String sql : sqls) {
                if (!sql.isEmpty()) {
                    PreparedStatement  createPs = this.dbConnTo.prepareStatement(sql);
                    createPs.executeUpdate();
                }
            }
        }else{
            String clearTableSql = "truncate table " + dbTo.getName() + "." + tableName;
            logger.info(String.format("truncate table for %s is begin...", tableName));
            PreparedStatement pStemt = this.dbConnTo.prepareStatement(clearTableSql);
            pStemt.executeUpdate();
        }
        this.dbConnTo.commit();
        currentProgress+=5;
        return true;

    }

}

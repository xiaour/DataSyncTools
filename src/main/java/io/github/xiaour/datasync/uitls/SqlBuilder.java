package io.github.xiaour.datasync.uitls;

/**
 * @author zhangtao
 * @Class SqlBuilder
 * @Description
 * @Date 2020/6/28 15:58
 * @Version 1.0.0
 */
public class SqlBuilder {


    public static String buildInsertSql(String table, String[] fields) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(table).append(" (");
        sb.append(String.join(", ", fields));
        sb.append(") ");
        sb.append("VALUES (");
        for (int i = 0; i < fields.length; i++) {
            if (i == fields.length - 1) {
                sb.append("?").append(")");
                break;
            }
            sb.append("?").append(", ");
        }
        return sb.toString();
    }
}

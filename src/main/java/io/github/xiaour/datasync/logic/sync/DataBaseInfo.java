package io.github.xiaour.datasync.logic.sync;

import java.util.Set;

/**
 * @author zhangtao
 * @Class DataBaseInfo
 * @Description
 * @Date 2020/6/28 15:31
 * @Version 1.0.0
 */
public class DataBaseInfo {

    private String host;

    private String name;

    private String user;

    private String password;

    private Set<String> tables;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getTables() {
        return tables;
    }

    public void setTables(Set<String> tables) {
        this.tables = tables;
    }
}

package com.luoboduner.wesync.logic.bean;

/**
 * @author zhangtao
 * @Class CombBoxItem
 * @Description
 * @Date 2020/6/29 10:44
 * @Version 1.0.0
 */
public class CombBoxItem {
    private String key;
    private String value;

    public CombBoxItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

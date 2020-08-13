package de.kaiserpfalzedv.rpg.admin.dao;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author kostenko
 */
public class MockData {
    
    private String customer;
    private LinkedHashMap<String, Long> dataMap;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public LinkedHashMap<String, Long> getDataMap() {
        return dataMap;
    }

    public void setDataMap(LinkedHashMap<String, Long> dataMap) {
        this.dataMap = dataMap;
    }

}

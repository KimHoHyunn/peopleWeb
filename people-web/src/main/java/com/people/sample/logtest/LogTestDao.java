package com.people.sample.logtest;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("LogTestDao")
public interface LogTestDao {
    List<Map<String, Object>> getData(Map<String, Object> params) ;
}

package com.people.sample.restapi;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("RestApiDao")
public interface RestApiDao {
    List<Map<String, Object>> getData(Map<String, Object> params) ;
}

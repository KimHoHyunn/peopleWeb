package com.people.sample.async.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("AsyncDao")
public interface AsyncDao {
    List<Map<String, Object>> getList() ;
}

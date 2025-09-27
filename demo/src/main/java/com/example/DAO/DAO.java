package com.example.DAO;


import java.util.List;

public interface DAO {
    void insert(Object obj) throws Exception;
    List<Object> getAll() throws Exception;
    boolean alreadyExist(Object obj) throws Exception;
    int count() throws Exception;
}

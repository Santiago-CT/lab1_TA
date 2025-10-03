package com.example.persistence;

import com.example.dataTransfer.DataTransfer;

import java.util.List;

public interface Persistence {
    void insert(DataTransfer obj) throws Exception;
    List<DataTransfer> getAll() throws Exception;
    boolean alreadyExist(DataTransfer obj) throws Exception;
    int count() throws Exception;
}

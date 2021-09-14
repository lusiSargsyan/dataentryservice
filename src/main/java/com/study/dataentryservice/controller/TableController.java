package com.study.dataentryservice.controller;

import com.study.dataentryservice.model.result.Result;
import com.study.dataentryservice.model.table.AlterTableRecord;
import com.study.dataentryservice.model.table.Table;
import com.study.dataentryservice.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
public class TableController {

    @Autowired
    private TableService service;

    @PutMapping(
            value = "/api/v1/create/table", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody Table table) {
        try {
            return ResponseEntity.ok().body(service.createTable(table).toString());
        } catch (Throwable e) {
            return ResponseEntity.badRequest().body(Result.errorResultString(e));
        }
    }

    @PutMapping(
            value = "/api/v1/alter/table", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> alter(@Valid @RequestBody AlterTableRecord alterTableRecord) {
        try {
            return ResponseEntity.ok().body(service.alterTable(alterTableRecord).toString());
        } catch (Throwable e) {
            return ResponseEntity.badRequest().body(Result.errorResultString(e));
        }
    }

    @DeleteMapping(
            value = "/api/v1/drop/table/{tableName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> drop(@PathVariable String tableName) {
        try {
            return ResponseEntity.ok().body(service.dropTable(tableName).toString());
        } catch (Throwable e) {
            return ResponseEntity.badRequest().body(Result.errorResultString(e));
        }
    }
}

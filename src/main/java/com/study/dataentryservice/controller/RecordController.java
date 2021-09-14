package com.study.dataentryservice.controller;

import com.study.dataentryservice.model.record.InsertRecord;
import com.study.dataentryservice.model.record.UpdateRecord;
import com.study.dataentryservice.model.result.Result;
import com.study.dataentryservice.service.RecordService;
import com.study.dataentryservice.service.ValidationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private ValidationService validationService;

    @PostMapping(
            value = "/api/v1/create/record", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@Valid @RequestBody InsertRecord record) {
        try {
            long mainRecordId = recordService.addRecord(record);
            return ResponseEntity.ok().body(Result.successResult("Successfully saved: " + mainRecordId).toString());
        } catch (Throwable e) {
            return ResponseEntity.badRequest().body(Result.errorResultString(e));
        }
    }

    @PutMapping(
            value = "/api/v1/update/record", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@Valid @RequestBody UpdateRecord updateRecord) {
        try {
            recordService.updateRecord(updateRecord);
            return ResponseEntity.ok().body(Result.successResultString());
        } catch (Throwable e) {
            return ResponseEntity.badRequest().body(Result.errorResultString(e));
        }
    }

    @GetMapping(
            value = "/api/v1/get/record/{tableName}/{columnName}/{value}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@PathVariable String tableName, @PathVariable String columnName, @PathVariable Object value) {
        try {
            JSONObject data = recordService.get(tableName, columnName, value);
            return ResponseEntity.ok().body(data.toString());
        } catch (Throwable e) {
            return ResponseEntity.badRequest().body(Result.errorResultString(e));
        }
    }

    @DeleteMapping(
            value = "/api/v1/delete/record/{tableName}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable String tableName, @PathVariable int id) {
        try {
            recordService.delete(tableName, id);
            return ResponseEntity.ok().body(Result.successResultString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Result.errorResultString(e));
        }
    }
}

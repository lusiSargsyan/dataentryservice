package com.study.dataentryservice.integrationTests;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataEntryServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void createTable() throws Exception {
        String CREATE_TABLE = "{ \"name\": \"createTableIntTest\", \"fields\": [  {   \"name\": \"id\",   \"isPrimaryKey\": true,   \"isAutoIncrement\": true,   \"type\": \"INT\",   \"size\": 10  },  {   \"name\": \"varcharColumn\",   \"type\": \"VARCHAR\",   \"size\": 255,   \"validation\": {    \"validationStrategy\": \"REQUIRED\"   }  },  {   \"name\": \"dateColumn\",   \"type\": \"DATE\",   \"validation\": {    \"validationStrategy\": \"CUSTOM\",    \"strategyLogic\": \"boolean isValidDate(String date) { boolean isValid = false;try {java.time.LocalDate localDate = java.time.LocalDate.parse(date,java.time.format.DateTimeFormatter.ofPattern(\\\"uuuu-M-d\\\").withResolverStyle(java.time.format.ResolverStyle.STRICT)); isValid = java.time.Period.between(localDate, java.time.LocalDate.now()).getYears() > 18; }catch (Exception e) {isValid = false;}return isValid;}\"   }  },  {   \"name\": \"parentColumnId\",   \"type\": \"INT\",   \"size\": 10  } ], \"references\": [  {   \"referenceTable\": {    \"name\": \"createTableIntTestParent\",    \"fields\": [     {      \"name\": \"id\",      \"isPrimaryKey\": true,      \"isAutoIncrement\": true,      \"type\": \"INT\",      \"size\": 10     },     {      \"name\": \"varcharColumn\",      \"type\": \"VARCHAR\",      \"size\": 255,      \"validation\": {       \"validationStrategy\": \"REQUIRED\"      }     }],    \"references\": []   },   \"toReferenceColumn\": \"id\",   \"fromReferenceColumn\": \"parentColumnId\",   \"modifyStrategy\": {    \"onDelete\": \"CASCADE\",    \"onUpdate\": \"CASCADE\"   }  } ]}";
        this.mockMvc.perform(put("/api/v1/create/table")
                        .content(CREATE_TABLE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":\"SUCCESS\"}"));
    }

    @Test
    @Order(2)
    void alterTable() throws Exception {
        String ALTER_TABLE = "{  \"tableName\": \"createTableIntTestParent\",  \"alterStrategy\": \"ADD\",  \"column\": {   \"name\": \"floatValue\",   \"type\": \"FLOAT\",   \"defaultValue\": 1.2  } }";
        this.mockMvc.perform(put("/api/v1/alter/table")
                        .content(ALTER_TABLE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":\"SUCCESS\"}"));
    }

    @Test
    @Order(3)
    void addRecord() throws Exception {
        String ADD_RECORD = "{\"tableName\": \"createTableIntTestParent\",\"data\": {\"varcharColumn\": \"testName\",\"createTableIntTest\": [{\"foreignKeyColumn\" : \"parentColumnId\",\"data\": {\"varcharColumn\": \"Some Text\",\"dateColumn\": \"1991-01-01\"}},{\"foreignKeyColumn\" : \"parentColumnId\",\"data\": {\"varcharColumn\": \"Some Text2\",\"dateColumn\": \"2020-01-01\"}}]}}";
        this.mockMvc.perform(post("/api/v1/create/record")
                        .content(ADD_RECORD)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":\"SUCCESS\"}"));
    }

    @Test
    @Order(4)
    void addRecordInvalid() throws Exception {
        String ADD_RECORD = "{\"tableName\": \"createTableIntTestParent\",\"data\": {\"createTableIntTest\": [{\"foreignKeyColumn\" : \"parentColumnId\",\"data\": {\"varcharColumn\": \"Some Text\",\"dateColumn\": \"1991-01-01\"}},{\"foreignKeyColumn\" : \"parentColumnId\",\"data\": {\"varcharColumn\": \"Some Text2\",\"dateColumn\": \"2020-01-01\"}}]}}";
        this.mockMvc.perform(post("/api/v1/create/record")
                        .content(ADD_RECORD)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":\"INVALID_INPUT\",\"message\":\"Invalid key: varcharColumn\"}"));
    }

    @Test
    @Order(5)
    public void dropTable() throws Exception {
        this.mockMvc.perform(delete("/api/v1/drop/table/createTableIntTest")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":\"SUCCESS\"}"));

        this.mockMvc.perform(delete("/api/v1/drop/table/createTableIntTestParent")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":\"SUCCESS\"}"));
    }
}

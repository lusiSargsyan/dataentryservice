package com.study.dataentryservice.model.table.column.validation.validationStrategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomValidationHelperTest {


    @Test
    public void executeSnippetTest() {
       String codeSnippet = "boolean testFunction(Object value) { return true;}";
       var dummyObject = new Object();
       Assertions.assertTrue(CustomValidationHelper.executeSnippet(dummyObject, codeSnippet));

        Assertions.assertTrue(CustomValidationHelper.executeSnippet(11,
                "boolean testFunction(int value) { return value > 10;}"));

        Assertions.assertFalse(CustomValidationHelper.executeSnippet(5,
                "boolean testFunction(int value) { return value > 10;}"));
    }

}

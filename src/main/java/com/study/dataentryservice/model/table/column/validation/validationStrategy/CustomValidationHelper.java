package com.study.dataentryservice.model.table.column.validation.validationStrategy;

import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;
import java.util.List;

public final class CustomValidationHelper {

    static boolean executeSnippet(Object value, String snippet) {
        JShell jshell = JShell.create();
        jshell.eval(snippet);
        List<SnippetEvent> result = jshell.eval(getMethodCall(getMethodName(snippet), value));
        return Boolean.parseBoolean(result.get(0).value());
    }

    //expected snippet example
    //boolean isValidDate(String date) { boolean isValid = false;try {java.time.LocalDate localDate = java.time.LocalDate.parse(date,java.time.format.DateTimeFormatter.ofPattern(\"uuuu-M-d\").withResolverStyle(java.time.format.ResolverStyle.STRICT)); isValid = java.time.Period.between(localDate, java.time.LocalDate.now()).getYears() > 18; }catch (Exception e) {isValid = false;}return isValid;}
    private static String getMethodCall(String methodName, Object value) {
       return methodName + "(\"" + value + "\")";
    }

    /**
     * Will extract method name from given snipped
     * @param snippet that needs to be executed
     * @return function name
     */
    private static String getMethodName(String snippet) {
        String methodSignature = snippet.substring(0, snippet.indexOf("("));
        return methodSignature.substring(methodSignature.lastIndexOf(" "));
    }
}

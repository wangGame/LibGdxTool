package com.libGdx.test.json.tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
public class JsonToJavaBeanGenerator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 生成 JavaBean 类
    public static void generateJavaBeans(String json, String className) throws IOException {
        JsonNode rootNode = objectMapper.readTree(json);
        StringBuilder javaClass = new StringBuilder();

        // 生成类头
        javaClass.append("public class ").append(className).append(" {\n\n");

        // 递归分析 JSON 并生成字段和 getter/setter
        generateFieldsAndMethods(rootNode, javaClass, className);

        // 生成类尾
        javaClass.append("\n}\n");

        // 输出主类文件
        writeToFile(javaClass.toString(), className);

        // 对所有嵌套的类单独生成文件
        generateNestedClasses(rootNode);
    }

    // 递归解析 JSON 并生成字段和方法
    private static void generateFieldsAndMethods(JsonNode node, StringBuilder javaClass, String className) {
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();


        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String fieldName = field.getKey();
            JsonNode fieldValue = field.getValue();

            // 判断字段的类型
            if (fieldValue.isObject()) {
                // 对象类型，递归调用生成新的类
                String fieldClassName = capitalizeFirstLetter(fieldName);
                javaClass.append("    private ").append(fieldClassName).append(" ").append(fieldName).append(";\n");
                generateFieldsAndMethods(fieldValue, javaClass, fieldClassName);
            }else if (fieldValue.isArray()) {
                // 处理数组类型
                String arrayType = getArrayType(fieldValue);
                javaClass.append("private List<").append(arrayType).append("> ").append(fieldName).append(";\n");

                // 处理数组元素类型
                ArrayNode fieldValue1 = (ArrayNode) fieldValue;
                for (JsonNode firstElement : fieldValue1) {
                    if (firstElement != null && firstElement.isObject()) {
                        String nestedClassName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        generateFieldsAndMethods(firstElement, javaClass,  nestedClassName);
                    }
                }

            }  else {
                // 基本数据类型
                String fieldType = getFieldType(fieldValue);
                javaClass.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n");
            }
            generateGetterSetter(getFieldType(fieldValue), fieldName, javaClass);
        }
    }


    // 获取数组元素的类型
    private static String getArrayType(JsonNode arrayNode)  {
        JsonNode firstElement = arrayNode.get(0);
        if (firstElement != null && firstElement.isObject()) {
            return firstElement.fieldNames().next();  // 假设第一个元素的类型为数组类型
        } else {
            return "Object";  // 默认数组类型
        }
    }


    // 生成 getter 和 setter 方法
    private static void generateGetterSetter(String fieldType, String fieldName, StringBuilder javaClass) {
        String capitalizedField = capitalizeFirstLetter(fieldName);

        // 生成 getter 方法
        javaClass.append("\n    public ").append(fieldType).append(" get").append(capitalizedField).append("() {\n");
        javaClass.append("        return ").append(fieldName).append(";\n");
        javaClass.append("    }\n");

        // 生成 setter 方法
        javaClass.append("\n    public void set").append(capitalizedField).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n");
        javaClass.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
        javaClass.append("    }\n");
    }

    // 获取字段类型
    private static String getFieldType(JsonNode node) {
        if (node.isTextual()) {
            return "String";
        } else if (node.isInt() || node.isLong()) {
            return "int";
        } else if (node.isDouble() || node.isFloat()) {
            return "double";
        } else if (node.isBoolean()) {
            return "boolean";
        } else {
            return "String";  // 默认返回 String 类型
        }
    }

    // 首字母大写
    private static String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // 将 Java 类写入文件
    private static void writeToFile(String classContent, String className) throws IOException {
        File file = new File(className + ".java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(classContent);
        }
    }

    // 生成嵌套类文件
    private static void generateNestedClasses(JsonNode node) throws IOException {
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            JsonNode fieldValue = field.getValue();
            if (fieldValue.isObject()) {
                String fieldClassName = capitalizeFirstLetter(field.getKey());
                StringBuilder nestedClass = new StringBuilder();

                // 生成类头
                nestedClass.append("public class ").append(fieldClassName).append(" {\n");

                // 递归生成字段和 getter/setter
                generateFieldsAndMethods(fieldValue, nestedClass, fieldClassName);

                // 生成类尾
                nestedClass.append("\n}\n");

                // 写入文件
                writeToFile(nestedClass.toString(), fieldClassName);
            }
        }
    }

    public static void main(String[] args) {
        // 示例 JSON 数据
        String json = "{ \"name\": \"John Doe\", \"age\": 30, \"address\": { \"street\": \"123 Main St\", \"city\": \"Anytown\", \"zip\": \"12345\" },\"student\":[{\"name\":\"chen\"}]}";

        try {
            // 生成 JavaBean 类
            generateJavaBeans(json, "Person");
            System.out.println("JavaBean classes generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.libGdx.test.json.tool;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.Iterator;
import java.util.Map;

public class JsonToJavaClassGenerator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 解析 JSON 字符串并生成相应的 Java 类
    public static String generateJavaClassFromJson(String json) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(json);
        StringBuilder classBuilder = new StringBuilder();
        classBuilder.append("public class GeneratedClass {\n");

        // 生成字段
        generateClassFields(jsonNode, classBuilder, "    ", null);

        classBuilder.append("}\n");
        return classBuilder.toString();
    }

    // 递归地为 JSON 中的字段生成 Java 类字段
    private static void generateClassFields(JsonNode jsonNode, StringBuilder classBuilder, String indent, String parentField) throws Exception {
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String fieldName = field.getKey();
            JsonNode fieldValue = field.getValue();

            String fieldType = getFieldType(fieldValue);

            // 如果是嵌套对象或者数组，生成一个新类
            if (fieldValue.isObject()) {
                String nestedClassName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                classBuilder.append(indent).append("private ").append(nestedClassName).append(" ").append(fieldName).append(";\n");
                // 递归生成嵌套类
                generateClassFields(fieldValue, classBuilder, indent + "    ", nestedClassName);
            } else if (fieldValue.isArray()) {
                // 处理数组类型
                String nestedClassName1 = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                classBuilder.append(indent).append("private List<").append(nestedClassName1).append("> ").append(fieldName).append(";\n");
                // 处理数组元素类型
                ArrayNode fieldValue1 = (ArrayNode) fieldValue;
                for (JsonNode firstElement : fieldValue1) {
                    if (firstElement != null && firstElement.isObject()) {
                        String nestedClassName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        generateClassFields(firstElement, classBuilder, indent + "    ", nestedClassName);
                    }
                }
            } else {
                classBuilder.append(indent).append("private ").append(fieldType).append(" ").append(fieldName).append(";\n");
            }
        }
    }

    // 根据字段的类型返回 Java 类型
    private static String getFieldType(JsonNode fieldValue) throws Exception {
        if (fieldValue.isObject()) {
            return "Object";  // 嵌套对象
        } else if (fieldValue.isArray()) {
            return "List<Object>";  // 默认处理为 List
        } else if (fieldValue.isTextual()) {
            return "String";
        } else if (fieldValue.isInt()) {
            return "int";
        } else if (fieldValue.isDouble()) {
            return "double";
        } else if (fieldValue.isBoolean()) {
            return "boolean";
        } else if (fieldValue.isNull()) {
            return "Object";
        } else {
            return "String";  // 默认处理为 String
        }
    }

    // 获取数组元素的类型
    private static String getArrayType(JsonNode arrayNode) throws Exception {
        JsonNode firstElement = arrayNode.get(0);
        if (firstElement != null && firstElement.isObject()) {
            return firstElement.fieldNames().next();  // 假设第一个元素的类型为数组类型
        } else {
            return "Object";  // 默认数组类型
        }
    }

    // 将 Java 对象序列化为 JSON
    public static String serializeObjectToJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    // 将 JSON 反序列化为 Java 对象
    public static <T> T deserializeJsonToObject(String json, Class<T> clazz) throws Exception {
        return objectMapper.readValue(json, clazz);
    }

    public static void main(String[] args) {
        String json = "{\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 30,\n" +
                "  \"address\": {\n" +
                "    \"street\": \"123 Main St\",\n" +
                "    \"city\": \"Anytown\",\n" +
                "    \"zip\": \"12345\"\n" +
                "  },\n" +
                "  \"student\": [\n" +
                "    {\"name\": \"chen\"},\n" +
                "    {\"age\": \"3\"}\n" +
                "  ]\n" +
                "}";


        try {
            // 生成 Java 类代码
            String generatedClass = generateJavaClassFromJson(json);
            System.out.println("Generated Java Class:\n" + generatedClass);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

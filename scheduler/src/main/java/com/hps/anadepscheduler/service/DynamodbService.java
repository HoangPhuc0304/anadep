package com.hps.anadepscheduler.service;

import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Map;

public interface DynamodbService {
    Map<String, AttributeValue> getItem(String tableName, Map<String, AttributeValue> key);
    Map<String, List<Map<String, AttributeValue>>> getBatchItem(String tableName, KeysAndAttributes keys);
    List<Map<String, AttributeValue>> query(String tableName, String keyExpression, Map<String, String> names, Map<String, AttributeValue> values);
    void putItem(String tableName, Map<String, AttributeValue> item);
    void updateItem(String tableName, Map<String, AttributeValue> key, Map<String, AttributeValueUpdate> attributeUpdates);
    void writeBatchItems(String tableName, List<WriteRequest> writeRequests);
    void deleteItem(String tableName, Map<String, AttributeValue> key);
}

package com.hps.anadepscheduler.service.impl;

import com.hps.anadepscheduler.service.DynamodbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Map;

@Service
public class DynamodbServiceImpl implements DynamodbService {
    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Override
    public Map<String, AttributeValue> getItem(String tableName, Map<String, AttributeValue> key) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();
        return dynamoDbClient.getItem(request).item();
    }

    @Override
    public Map<String, List<Map<String, AttributeValue>>> getBatchItem(String tableName, KeysAndAttributes keys) {
        BatchGetItemRequest batchGetItemRequest = BatchGetItemRequest.builder()
                .requestItems(Map.of(tableName, keys))
                .build();
        return dynamoDbClient.batchGetItem(batchGetItemRequest).responses();
    }

    @Override
    public List<Map<String, AttributeValue>> query(String tableName, String keyExpression, Map<String, String> names, Map<String, AttributeValue> values) {
        QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression(keyExpression)
                .expressionAttributeNames(names)
                .expressionAttributeValues(values)
                .build();
        QueryResponse response = dynamoDbClient.query(request);
        return response.items();
    }

    @Override
    public void putItem(String tableName, Map<String, AttributeValue> item) {
        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();
        dynamoDbClient.putItem(request);
    }

    @Override
    public void updateItem(String tableName, Map<String, AttributeValue> key, Map<String, AttributeValueUpdate> attributeUpdates) {
        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .attributeUpdates(attributeUpdates)
                .build();
        dynamoDbClient.updateItem(request);
    }

    @Override
    public void writeBatchItems(String tableName, List<WriteRequest> writeRequests) {
        BatchWriteItemRequest batchWriteItemRequest = BatchWriteItemRequest.builder()
                .requestItems(Map.of(tableName, writeRequests))
                .build();
        dynamoDbClient.batchWriteItem(batchWriteItemRequest);
    }

    @Override
    public void deleteItem(String tableName, Map<String, AttributeValue> key) {
        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();
        dynamoDbClient.deleteItem(request);
    }
}

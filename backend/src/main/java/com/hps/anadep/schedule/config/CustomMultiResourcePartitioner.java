//package com.hps.osvscanning.schedule.config;
//
//import org.springframework.batch.core.partition.support.Partitioner;
//import org.springframework.batch.item.ExecutionContext;
//import org.springframework.core.io.Resource;
//import org.springframework.util.Assert;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class CustomMultiResourcePartitioner implements Partitioner {
//    private static final String KEY_NAME = "fileName";
//
//    private static final String PARTITION_KEY = "partition";
//
//    private Resource[] resources = new Resource[0];
//
//    public void setResources(Resource[] resources) {
//        this.resources = resources;
//    }
//
//    @Override
//    public Map<String, ExecutionContext> partition(int gridSize) {
//        Map<String, ExecutionContext> map = new HashMap<>(gridSize);
//        int i = 0;
//        for (Resource resource : resources) {
//            ExecutionContext context = new ExecutionContext();
//            Assert.state(resource.exists(), "Resource does not exist: " + resource);
//            context.putString(KEY_NAME, resource.getFilename());
//            map.put(PARTITION_KEY + i, context);
//            i++;
//        }
//        return map;
//    }
//
////    @Override
////    public Map<String, ExecutionContext> partition(int gridSize) {
////        Map<String, ExecutionContext> map = new HashMap<>();
////        int i = 0;
////        for (int j = 0; j < resources.length; j += gridSize) {
////            ExecutionContext context = new ExecutionContext();
////            for (int k = 0; k < gridSize && j + k < resources.length; k++) {
////                Resource resource = resources[j + k];
////                Assert.state(resource.exists(), "Resource does not exist: " + resource);
////                context.putString(KEY_NAME + k, resource.getFilename());
////            }
////            map.put(PARTITION_KEY + i, context);
////            i++;
////        }
////        return map;
////    }
//}

package com.project.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.QueueService.QueueService;
import com.project.datastructures.HashMap;
import com.project.datastructures.Linkedlist;

@RestController
@RequestMapping("/api/queue")
public class QueueRestController {

    // Store request ids and their results - will be used in future implementation
    // private static final ConcurrentHashMap<String, String> requestResults = new ConcurrentHashMap<>();

    /**
     * Handles HTTP requests to enqueue messages
     * 
     * @param message The message content to enqueue
     * @param queueName The queue name from headers
     * @return A response with confirmation or error
     */
    @PostMapping("/enqueue")
    public ResponseEntity<String> enqueueMessage(
            @RequestBody String message,
            @RequestHeader("QueueName") String queueName) {
        
        try {
            // Create queue if it doesn't exist
            if (!QueueService.queueExists(queueName)) {
                QueueService.createQueue(queueName);
            }
            
            QueueService.enqueue(queueName, message);
            return ResponseEntity.ok("Message added to queue '" + queueName + "'");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to enqueue message: " + e.getMessage());
        }
    }
    
    /**
     * Handles HTTP requests to dequeue messages
     * 
     * @param queueName The queue name from headers
     * @return The dequeued message or status message
     */
    @GetMapping("/dequeue")
    public ResponseEntity<String> dequeueMessage(
            @RequestHeader("QueueName") String queueName) {
        
        try {
            // First check if queue exists
            if (!QueueService.queueExists(queueName)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Queue '" + queueName + "' doesn't exist.");
            }

            String data = QueueService.dequeue(queueName);
            if (data != null) {
                return ResponseEntity.ok(data);
            } else {
                // Queue exists but is empty
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("Queue '" + queueName + "' is empty.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to dequeue from queue '" + queueName + "': " + e.getMessage());
        }
    }
    
    /**
     * Check if a queue exists
     * 
     * @param queueName The queue name from path
     * @return Status of the queue
     */
    @GetMapping("/exists/{queueName}")
    public ResponseEntity<Boolean> queueExists(@PathVariable String queueName) {
        return ResponseEntity.ok(QueueService.queueExists(queueName));
    }
    
    /**
     * Check status of a request or get its result
     * 
     * @param queueName The queue name from header
     * @param requestId The request ID from header
     * @return The result of the request or status information
     */
    @GetMapping("/status")
    public ResponseEntity<?> getRequestStatus(
            @RequestHeader("QueueName") String queueName,
            @RequestHeader("RequestId") String requestId) {
        
        // For demonstration purposes, just return a simple response
        // In a real implementation, this would check a database or cache for results
        HashMap<String, Object> result = new HashMap<>();
        result.put("requestId", requestId);
        result.put("status", "COMPLETED");
        result.put("restaurantId", "123456");
        
        // Create a sample path from Taksim to Sultanahmet
        Linkedlist<HashMap<String, Double>> path = new Linkedlist<>();
        
        // Add Taksim point
        HashMap<String, Double> point1 = new HashMap<>();
        point1.put("latitude", 41.0370);
        point1.put("longitude", 28.9850);
        path.add(point1);
        
        // Add other points
        HashMap<String, Double> point2 = new HashMap<>();
        point2.put("latitude", 41.0352);
        point2.put("longitude", 28.9775);
        path.add(point2);
        
        HashMap<String, Double> point3 = new HashMap<>();
        point3.put("latitude", 41.0314);
        point3.put("longitude", 28.9752);
        path.add(point3);
        
        HashMap<String, Double> point4 = new HashMap<>();
        point4.put("latitude", 41.0272);
        point4.put("longitude", 28.9744);
        path.add(point4);
        
        HashMap<String, Double> point5 = new HashMap<>();
        point5.put("latitude", 41.0223);
        point5.put("longitude", 28.9747);
        path.add(point5);
        
        HashMap<String, Double> point6 = new HashMap<>();
        point6.put("latitude", 41.0159);
        point6.put("longitude", 28.9768);
        path.add(point6);
        
        // Add Sultanahmet point
        HashMap<String, Double> point7 = new HashMap<>();
        point7.put("latitude", 41.0082);
        point7.put("longitude", 28.9784);
        path.add(point7);
        
        result.put("path", path);
        result.put("distance", 3500.0); // 3.5 km
        result.put("duration", 2400); // 40 minutes
        
        return ResponseEntity.ok(result);
    }
}

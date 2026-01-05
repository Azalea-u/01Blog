package com.example.blog.model;

import java.io.Serializable;

public class SubscriptionId implements Serializable {
    private Long subscriber;
    private Long target;

    public SubscriptionId() {}
    public SubscriptionId(Long subscriber, Long target) {
        this.subscriber = subscriber;
        this.target = target;
    }

    // Getters and setters
    public Long getSubscriber() { return subscriber; }
    public void setSubscriber(Long subscriber) { this.subscriber = subscriber; }
    
    public Long getTarget() { return target; }
    public void setTarget(Long target) { this.target = target; }
}

package com.example.blog.repository;

import com.example.blog.model.Subscription;
import com.example.blog.model.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {

    List<Subscription> findBySubscriberId(Long subscriberId);

    long countByTargetId(Long targetId);

    boolean existsBySubscriberIdAndTargetId(Long subscriberId, Long targetId);
}

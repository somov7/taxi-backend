package com.catghoti.taxi.repository;

import com.catghoti.taxi.model.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    Optional<Order> findById(ObjectId id);
    Order save(Order order);
}

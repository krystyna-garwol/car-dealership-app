package uk.sky.cardealer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import uk.sky.cardealer.model.Booking;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {

    List<Booking> findAllByUserId(String userId);

    List<Booking> findAllByCarId(String carId);

    List<Booking> findAllByStartDate(Date startDate);

    List<Booking> findAllByEndDate(Date endDate);


    @Query("{ 'startDate': { '$gte': ?0 } ,  'endDate': { '$lte': ?1 } }")
    List<Booking> findAllBetweenTwoDates(Date start, Date end);

    long count();
}

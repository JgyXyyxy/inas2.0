package com.sudu.inas.repository;

import com.sudu.inas.beans.Event;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventRepository extends ElasticsearchRepository<Event,String> {

    Event queryEventByEventId(String eventId);
    void deleteEventByEventId(String eventId);
    List<Event> queryEventsByTs(String ts);
}

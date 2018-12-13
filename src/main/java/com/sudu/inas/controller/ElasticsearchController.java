package com.sudu.inas.controller;

import com.sudu.inas.beans.Event;
import com.sudu.inas.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/es")
public class ElasticsearchController {

    @Autowired
    private EventRepository eventRepository;

    @RequestMapping("/add")
    public void add(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        Event event = new Event();
        event.setEventId(str);
        event.setTs("1999-09-09");
        event.setSite("北京");
        event.setDetails("Unknown");
        event.setAffect("Unknown");
        Event save = eventRepository.save(event);
        System.out.println("add " + event);
    }

    @RequestMapping("/query")
    public void query(){
        String id = "bf09ffd4-d216-42e5-88b5-7a6e77d432c3";
        Event event = eventRepository.queryEventByEventId(id);
        System.out.println("query " + event);
        List<Event> events = eventRepository.queryEventsByTs("1999-09-09");
        for (Event tmp:events){
            System.out.println("tsQuery: "+tmp);
        }

    }


//    public static void main(String[] args) {
//        new ElasticsearchController().add();
//    }
}

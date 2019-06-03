package org.greta.eventreactive.web;


import io.netty.channel.epoll.Native;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@RestController
public class EventReactiveRestController {

    @GetMapping(value="/streamEvents/{id}",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Event> streamEvents(@PathVariable String id){
        Flux<Long> interval=Flux.interval(Duration.ofMillis(1000));
        Flux<Event>eventFlux=Flux.fromStream(Stream.generate(()->{
            Event event=new Event();
            event.setInstant(Instant.now());
            event.setSocieteID(id);
            event.setValue(100+Math.random()*1000);
             return  event;
        }));
        return  Flux.zip(interval,eventFlux)
                      .map(data->{
                          return data.getT2();
                      });
    }



}
@Data @AllArgsConstructor @NoArgsConstructor
class  Event{
    private Instant instant;
    private double value;
    private String societeID;
}

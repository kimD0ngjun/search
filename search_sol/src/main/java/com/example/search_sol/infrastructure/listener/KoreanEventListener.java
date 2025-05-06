package com.example.search_sol.infrastructure.listener;

import com.example.search_sol.application.event.CreateEvent;
import com.example.search_sol.application.event.DeleteEvent;
import com.example.search_sol.application.event.SimpleUpdateEvent;
import com.example.search_sol.application.event.UpdateEvent;

import java.io.IOException;

public interface KoreanEventListener {
    void listenCreateEvent(CreateEvent event) throws IOException;
    void listenUpdateEvent(UpdateEvent event) throws IOException;
    void listenSimpleUpdateEvent(SimpleUpdateEvent event) throws IOException;
    void listenDeleteEvent(DeleteEvent event) throws IOException;
}

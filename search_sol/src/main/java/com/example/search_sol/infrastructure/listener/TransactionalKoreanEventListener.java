package com.example.search_sol.infrastructure.listener;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.example.search_sol.application.dto.KoreanUpdateDTO;
import com.example.search_sol.application.dto.KoreanSimpleUpdateDTO;
import com.example.search_sol.application.event.CreateEvent;
import com.example.search_sol.application.event.DeleteEvent;
import com.example.search_sol.application.event.SimpleUpdateEvent;
import com.example.search_sol.application.event.UpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionalKoreanEventListener implements KoreanEventListener {

    private final ElasticsearchClient elasticsearchClient;

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listenCreateEvent(CreateEvent event) throws IOException {
        log.info("생성 이벤트 발생, {}", event.getId());
        elasticsearchClient.index(i -> i.index("koreans")
                .id(String.valueOf(event.getId()))
                .document(event.getDto()));
    }

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listenUpdateEvent(UpdateEvent event) throws IOException {
        log.info("전체 업데이트 이벤트 발생, {}", event.getId());
        elasticsearchClient.update(u -> u.index("koreans")
                        .id(String.valueOf(event.getId()))
                        .doc(event.getDto())
                        .docAsUpsert(true)
                , KoreanUpdateDTO.class);
    }

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listenSimpleUpdateEvent(SimpleUpdateEvent event) throws IOException {
        log.info("일부 업데이트 이벤트 발생, {}", event.getId());
        elasticsearchClient.update(u -> u.index("koreans")
                        .id(String.valueOf(event.getId()))
                        .doc(event.getDto())
                        .docAsUpsert(true)
                , KoreanSimpleUpdateDTO.class);
    }

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listenDeleteEvent(DeleteEvent event) throws IOException {
        log.info("삭제 이벤트 발생, {}", event.getId());
        elasticsearchClient.delete(d -> d.index("koreans")
                .id(String.valueOf(event.getId())));
    }
}

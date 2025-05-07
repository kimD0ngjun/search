# 사전 단어 검색(한국어 기반)
---
## 아키텍처
<img width="618" alt="스크린샷 2025-05-07 오후 5 30 10" src="https://github.com/user-attachments/assets/a1e4f36c-38bd-4908-be5a-336ed1dce43b" />

## 사용 스택
- `Spring Boot` : 사용자 검색 API, 관리자 데이터 추가 API
- `Elasticsearch` : 역색인 바탕 데이터 검색 엔진 & NoSQL
- `MySQL` : 트랜잭션 보장용 데이터 영속 보관소 & 데이터 관리
- `Spring Batch` : 대규모 MySQL 데이터 ES 이관 배치 처리
- `JavaScript` : 검색 데이터 페이징 처리 결과 반환
- `Python` : xls 원본 데이터 csv 컨버팅

## 구현 결과
![final](https://github.com/user-attachments/assets/d1382085-73ab-44eb-bc6e-af7448158187)

## 트러블 슈팅

### 1) ES 인덱스 세팅 & 검색 조건 설정

```json
// index 초기화, PUT /koreans
{
  "settings": {
    "index": { "max_ngram_diff": 30 },
    "analysis": {
      "analyzer": {
        "my_custom_analyzer": {
          "type": "custom", "char_filter": [],
          "tokenizer": "my_nori_tokenizer",
          "filter": ["lowercase_filter", "synonym_filter"]
        },
        "autocomplete_analyzer": {
          "type": "custom", "tokenizer": "autocomplete_tokenizer", "filter": ["lowercase"]
        },
        "ngram_analyzer": {
          "type": "custom", "tokenizer": "ngram_tokenizer", "filter": ["lowercase"]
        },
        "chosung_analyzer": { // 나중에 추가하기
          "type": "custom", "tokenizer": "standard", "filter": ["lowercase"]
        }
      },
      "tokenizer": {
        "my_nori_tokenizer": {
          "type": "nori_tokenizer", "decompound_mode": "mixed", "discard_punctuation": "true",
          "user_dictionary": "dict/userdict_ko.txt", "lenient": true
        },
        "autocomplete_tokenizer": {
          "type": "edge_ngram", "min_gram": 2, "max_gram": 30, "token_chars": ["letter", "digit"]
        },
        "ngram_tokenizer": {
          "type": "ngram", "min_gram": 2, "max_gram": 30, "token_chars": ["letter", "digit"]
        }
      },
      "filter": {
        "lowercase_filter": { "type": "lowercase" },
        "synonym_filter": {
          "type": "synonym", "synonyms_path": "dict/synonym-set.txt", "lenient": true
        }
      },
      "normalizer": {
        "chosung_normalizer": { "type": "custom", "filter": ["lowercase"] }
      }
    }
  },
  "mappings": {
    "properties": {
      "entry": {
        "type": "text", "analyzer": "my_custom_analyzer",
        "fields": {
          "autocomplete": { "type": "text", "analyzer": "autocomplete_analyzer" },
          "ngram": { "type": "text", "analyzer": "ngram_analyzer" },
          "chosung": { "type": "keyword", "normalizer": "chosung_normalizer" } // 나중에 추가하기
        }
      },
      "type": { "type": "keyword" },
      "pos": { "type": "keyword" },
      "definition": {
        "type": "text", "analyzer": "my_custom_analyzer",
        "fields": {
          "ngram": { "type": "text", "analyzer": "ngram_analyzer" }
        }
      },
      "entry_chosung": {
        "type": "keyword", "normalizer": "chosung_normalizer",
        "fields": {
          "autocomplete": { "type": "text", "analyzer": "autocomplete_analyzer" },
          "ngram": { "type": "text", "analyzer": "ngram_analyzer" }
        }
      }
    }
  }
}
```
```json
// search 예시, GET /koreans/_search
{
  "size": 10,
  "query": {
    "dis_max": {
      "queries": [
        { "match": { "entry": { "query": "검색", "boost": 2 }}},
        { "match": { "entry.ngram": { "query": "검색", "boost": 1 }}},
        { "match": { "entry.autocomplete": { "query": "검색", "boost": 0.2 }}}
      ]
    }
  },
  "highlight": {
    "fields": {
      "entry": {
        "pre_tags": ["<strong>"],
        "post_tags": ["</strong>"]
      },
      "definition": {
        "pre_tags": ["<strong>"],
        "post_tags": ["</strong>"]
      }
    }
  }
}
```
- 향후 초성 기반 검색, 영문 포함 검색 확장하여 재인덱싱 예정
- WAS의 `ElasticsearchClient` 통해 추천 점수 바탕 정렬

### 2) 관리자 MySQL CRUD 실시간 ES 동기화

```java
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

    // ...
```

- `ApplicationEventPulisher` 의존성 주입
- 커스텀 이벤트 객체 리스너 등록

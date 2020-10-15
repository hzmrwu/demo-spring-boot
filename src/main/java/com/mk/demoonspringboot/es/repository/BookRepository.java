package com.mk.demoonspringboot.es.repository;

import com.mk.demoonspringboot.es.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author Mark
 * @date 2020/10/15
 */
public interface BookRepository extends ElasticsearchRepository<Book, String> {

    /*
    这个方法相当于如下原生查询
    {
        "query": {
            "bool" : {
                "must" : [
                    { "query_string" : { "query" : "?", "fields" : [ "name" ] } },
                    { "query_string" : { "query" : "?", "fields" : [ "price" ] } }
                ]
            }
        }
    }
    支持的方法关键字和返回值类型见 https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.repositories
    */
    List<Book> findByNameAndPrice(String name, Integer price);

    /*
    JSON查询，相当于如下原生查询
    {
      "query": {
        "match": {
          "name": {
            "query": "John"
          }
        }
      }
    }
    */
    @Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
    Page<Book> findByName(String name, Pageable pageable);
}

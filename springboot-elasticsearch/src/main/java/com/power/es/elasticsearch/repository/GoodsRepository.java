package com.power.es.elasticsearch.repository;

import com.power.es.elasticsearch.vo.GoodsInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;


@Component
public interface GoodsRepository extends ElasticsearchRepository<GoodsInfo,Long> {


}

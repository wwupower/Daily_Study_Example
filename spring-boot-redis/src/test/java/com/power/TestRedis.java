package com.power;

import com.power.lock.RedisLock;
import com.power.model.Order;
import com.power.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {

    Logger logger = LoggerFactory.getLogger(TestRedis.class);
    private final static int threadCount = 1;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RedisLock redisLock;

    private volatile int reqCount = 0;

    @Test
    public void test1() throws Exception {
        for(int i=1;i<10;i++){
            testIdInr();
            Thread.sleep(2000);
        }
    }

    @Test
    public void testIdInr() throws Exception {
        String id_KEY = "wwupower-test:@30PL_20190901";
        ExecutorService exec = Executors.newCachedThreadPool();
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            exec.execute(() -> {
                try {
                    Long id = getId2();
                    orderRepository.save(new Order().setOrderNo(id));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        exec.shutdown();
        Object id = redisTemplate.opsForValue().get(id_KEY);
        System.out.println("ID---------------------" + id);
    }


    public Long getId2() throws Exception {
        String id_KEY = "wwupower-test:@30PL_20190901";
        reqCount++;
        if (reqCount == 10) {
            redisTemplate.delete(id_KEY);
        }
        ValueOperations<String, Long> operations = redisTemplate.opsForValue();
        if (redisTemplate.hasKey(id_KEY)) {
            if (reqCount == 20) {
                redisTemplate.delete(id_KEY);
            }
            Long id = setExistIncr(Arrays.asList(id_KEY), 1);
            if (id > 0) {
                return id;
            }
        }
        //首次初始化
        synchronized (this) {
            if (redisTemplate.hasKey(id_KEY)) {
                return redisTemplate.opsForValue().increment(id_KEY);
            }
            logger.info(">>查询数据库最大值");
            Long idMax4db = orderRepository.getMaxId();
            if (null == idMax4db) {
                /*setNxEX(id_KEY, 0L, 24 * 60 * 60L);
                return operations.increment(id_KEY);*/
                Long id = setNxEXIncr(id_KEY, 0L, 24 * 60 * 60L,1);
                if(id == 0){
                    throw new Exception("订单创建失败----");
                }
                return id;
            }
            Long id = setNxEXIncr(id_KEY, idMax4db, 24 * 60 * 60L,1);
            if(id == 0){
                throw new Exception("订单创建失败----");
            }
            return id;
          /*  setNxEX(id_KEY, idMax4db, 24 * 60 * 60L);
            return operations.increment(id_KEY);*/
        }
    }

    public Long getId() {
        String id_KEY = "wwupower-test:@30PL_20190901";
        reqCount++;
        if (reqCount == 10) {
            redisTemplate.delete(id_KEY);
        }
        ValueOperations<String, Long> operations = redisTemplate.opsForValue();
        if (redisTemplate.hasKey(id_KEY)) {
            if (reqCount == 20) {
                redisTemplate.delete(id_KEY);
            }
            Long id = setExistIncr(Arrays.asList(id_KEY), 1);
            if (id > 0) {
                return id;
            }
        }
        //首次初始化
        boolean lock = redisLock.lock(id_KEY + "_lock", Thread.currentThread().getId() + "");
        if (lock) {
        }
        return getId();

    }


    public String idFormat(Long id, int length) {
        String idFormat = String.format("%0" + length + "d", id);
        return idFormat;
    }

    private String dateFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * @return
     */
    public Long setNxEXIncr(String key, Long value,Long expire,int incre) {
        String script = "local result = redis.call('SETNX',KEYS[1],ARGV[1]);" +
                "if result == 1 then  " +
                "redis.call('expire',KEYS[1],ARGV[2]);" +
                "return redis.call('INCRBY',KEYS[1],ARGV[3]);" +
                "end;" ;
        DefaultRedisScript<Long> script1 = new DefaultRedisScript<>();
        script1.setScriptText(script);
        script1.setResultType(Long.class);
        Object result = redisTemplate.execute(script1,Arrays.asList(key), value,expire,incre);
        return (Long) result;

    }

    /**
     * @return
     */
    public Boolean setNxEX(String key, Long value,Long expire) {
        String script = "local result = redis.call('SETNX',KEYS[1],ARGV[1]);" +
                "if result == 1 then  " +
                "redis.call('expire',KEYS[1],ARGV[2]);" +
                "return true " +
                "end; " +
                "return false";
        DefaultRedisScript<Boolean> script1 = new DefaultRedisScript<>();
        script1.setScriptText(script);
        script1.setResultType(Boolean.class);
        Object result = redisTemplate.execute(script1,Arrays.asList(key), value,expire);
        return (Boolean) result;

    }

    /**
     * @param keys
     * @param values
     * @return
     */
    public Long setExistIncr(List<String> keys, Object... values) {
        String script = "local result = redis.call('EXISTS',KEYS[1]);" +
                "if result == 1 then  " +
                "return redis.call('INCRBY',KEYS[1],ARGV[1]);" +
                "end; " +
                "return 0";
        DefaultRedisScript<Long> script1 = new DefaultRedisScript<>();
        script1.setScriptText(script);
        script1.setResultType(Long.class);
        Object result = redisTemplate.execute(script1, keys, values);
        return (Long) result;

    }
}
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
    private final static int threadCount = 30;

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


    public Long getId2() {
        String id_KEY = "wwupower-test:@30PL_20190901";
        if(reqCount++ == 10){
            redisTemplate.delete(id_KEY);
        }
        if(reqCount++ == 20){
            redisTemplate.delete(id_KEY);
        }
        ValueOperations<String, Long> operations = redisTemplate.opsForValue();
        if (redisTemplate.hasKey(id_KEY)) {
            return redisTemplate.opsForValue().increment(id_KEY);
        }
        //首次初始化
        synchronized (this){
            if(redisTemplate.hasKey(id_KEY)){
                return redisTemplate.opsForValue().increment(id_KEY);
            }
            logger.info(">>查询数据库最大值");
            Long idMax4db = orderRepository.getMaxId();
            if (null == idMax4db) {
                setNxEX(Arrays.asList(id_KEY),0L,24*60*60);
                return operations.increment(id_KEY);
            }
            setNxEX(Arrays.asList(id_KEY),idMax4db,24*60*60);
            return redisTemplate.opsForValue().increment(id_KEY);
        }
    }

    public Long getId() {
        String id_KEY = "wwupower-test:@30PL_20190901";
        ValueOperations<String, Long> operations = redisTemplate.opsForValue();
        Object id = operations.get(id_KEY);
        if (id != null) {
            id = operations.get(id_KEY);
            if(id != null){
                return redisTemplate.opsForValue().increment(id_KEY);
            }
        }
        //首次初始化
        boolean lock = redisLock.lock(id_KEY+"_lock",Thread.currentThread().getId()+"");
        if(lock){
            Long idMax4db = orderRepository.getMaxId();
            if (null == idMax4db) {
                setNxEX(Arrays.asList(id_KEY),0L,24*60*60);
                return redisTemplate.opsForValue().increment(id_KEY);
            }
            setNxEX(Arrays.asList(id_KEY),idMax4db,24*60*60);
            redisLock.unlock(id_KEY+"_lock",Thread.currentThread().getId()+"");
            return redisTemplate.opsForValue().increment(id_KEY);
        }
        return getId();

    }


    public String idFormat(Long id,int length){
        String idFormat = String.format("%0"+length+"d",id);
        return idFormat;
    }

    private String dateFormat(Date date,String format) {
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
       return simpleDateFormat.format(date);
    }

    /**
     *
     * @param keys
     * @param values
     * @return
     */
    public Boolean setNxEX(List<String> keys, Object... values){
        String script = "local result = redis.call('SETNX',KEYS[1],ARGV[1]);" +
                "if result == 1 then  " +
                "redis.call('expire',KEYS[1],ARGV[2]);" +
                "return true " +
                "end; " +
                "return false";
        DefaultRedisScript<Boolean> script1 = new DefaultRedisScript<>();
        script1.setScriptText(script);
        script1.setResultType(Boolean.class);
        Object result = redisTemplate.execute(script1,keys,values);
        return (Boolean)result;

    }
}
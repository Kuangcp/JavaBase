package com.github.kuangcp.queue.disruptor.first;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Kuangcp
 * 2024-09-24 16:05
 * @link <a href="https://juejin.cn/post/6844904020973191181">高性能队列 Disruptor 使用教程 </a>
 */
public class FirstTest {

    @Test
    public void testSingleProductSinConsumer() throws Exception {
        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                OrderEvent::new,
                1024 * 1024,
                Executors.defaultThreadFactory(),
                ProducerType.SINGLE,
                new YieldingWaitStrategy()
        );
        disruptor.handleEventsWith(new OrderEventHandler());
        disruptor.start();
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderEventProducer eventProducer = new OrderEventProducer(ringBuffer);
        eventProducer.onData(UUID.randomUUID().toString());

        Thread.currentThread().join();
    }

    @Test
    public void testSingleProductMultiConsumer() throws Exception {
        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                OrderEvent::new,
                1024 * 1024,
                Executors.defaultThreadFactory(),
                ProducerType.SINGLE,
                new YieldingWaitStrategy()
        );
        disruptor.handleEventsWith(new OrderEventHandler(), new OrderEventHandler());
        disruptor.start();
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderEventProducer eventProducer = new OrderEventProducer(ringBuffer);
        eventProducer.onData(UUID.randomUUID().toString());

        Thread.currentThread().join();
    }

    /**
     * 多生产者 多消费者
     */
    @Test
    public void testProductConsumer() throws Exception {
        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                OrderEvent::new,
                1024 * 1024,
                Executors.defaultThreadFactory(),
                // 这里的枚举修改为多生产者
                ProducerType.MULTI,
                new YieldingWaitStrategy()
        );
        // 多个消费者不重复消费（一个消息只会被消费一次，但是消费者间可以并行处理消息）, 需要实现WorkHandler接口
        disruptor.handleEventsWithWorkerPool(new OrderEventHandler("a"), new OrderEventHandler("b"));
        disruptor.start();
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderEventProducer eventProducer = new OrderEventProducer(ringBuffer);
        // 创建一个线程池，模拟多个生产者
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            fixedThreadPool.execute(() -> eventProducer.onData(UUID.randomUUID().toString()));
        }

        Thread.currentThread().join(2000);
    }

    @Test
    public void testConsumerChain() throws Exception {
        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                OrderEvent::new,
                1024 * 1024,
                Executors.defaultThreadFactory(),
                ProducerType.SINGLE,
                new YieldingWaitStrategy()
        );

        disruptor.handleEventsWith(new OrderEventHandler())
                .thenHandleEventsWithWorkerPool(new OrderEventHandler("a"), new OrderEventHandler("b"), new OrderEventHandler("c"))
                .then(new OrderEventHandler());

        disruptor.start();
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderEventProducer eventProducer = new OrderEventProducer(ringBuffer);
        eventProducer.onData(UUID.randomUUID().toString());

        Thread.currentThread().join(3000);
    }


    /**
     * 多生产者 多消费者
     */
    @Test
    public void testProductConsumerSlow() throws Exception {
        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                OrderEvent::new,
                1024 * 1024,
                Executors.defaultThreadFactory(),
                // 这里的枚举修改为多生产者
                ProducerType.MULTI,
                new YieldingWaitStrategy()
        );
        // 有多少个消费者就会创建多少线程去执行消费，此时会创建三个线程
        disruptor
                .handleEventsWithWorkerPool(new OrderEventSlowHandler("a"), new OrderEventSlowHandler("b"))
                .then(new OrderEventSlowHandler("c"));
        disruptor.start();
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderEventProducer eventProducer = new OrderEventProducer(ringBuffer);
        // 创建一个线程池，模拟多个生产者
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            fixedThreadPool.execute(() -> eventProducer.onData(UUID.randomUUID().toString()));
        }

        Thread.currentThread().join();
    }
}

package com.github.kuangcp.sharding.manual;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author https://github.com/kuangcp on 2021-07-11 19:13
 */
public class ConsistentHashingAlgorithm {
    private MessageDigest md5;
    private final int numberOfReplicas = 300;

    private final SortedMap<Long, String> circle = new TreeMap<>();
    private static final Map<Integer, ConsistentHashingAlgorithm> cache = new HashMap<>();

    public ConsistentHashingAlgorithm(int totalNode) {
        for (int i = 1; i <= totalNode; i++) {
            add("_" + i);
        }
    }

    /**
     * TODO 线程不安全，以及环的多节点维护问题
     */
    public static ConsistentHashingAlgorithm useWithCache(int totalNode) {
        ConsistentHashingAlgorithm result = cache.get(totalNode);
        if (Objects.nonNull(result)) {
            return result;
        }
        ConsistentHashingAlgorithm newOne = new ConsistentHashingAlgorithm(totalNode);
        cache.put(totalNode, newOne);
        return newOne;
    }

    /**
     * 添加虚拟节点
     * numberOfReplicas为虚拟节点的数量
     * 例如初始化hash环的时候传入，我们使用300个虚拟节点
     */
    public void add(String node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.put(this.hash(node.toString() + "_" + i), node);
        }
    }

    /**
     * 移除节点
     */
    public void remove(String node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(this.hash(node.toString() + i));
        }
    }

    public String get(Long key) {
        return get(key + "");
    }

    /**
     * 获得一个最近的顺时针节点
     *
     * @param key 为给定键取Hash，取得顺时针方向上最近的一个虚拟节点对应的实际节点
     */
    public String get(String key) {
        if (circle.isEmpty()) {
            return null;
        }
        long hash = this.hash(key);
        if (!circle.containsKey(hash)) {
            //返回此映射的部分视图，其键大于等于 hash
            SortedMap<Long, String> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

    /**
     * TODO 线程不安全
     */
    public long hash(String key) {
        if (md5 == null) {
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("no md5 algorythm found");
            }
        }

        md5.reset();
        md5.update(key.getBytes());
        byte[] bKey = md5.digest();

        long res = ((long) (bKey[3] & 0xFF) << 24) |
                ((long) (bKey[2] & 0xFF) << 16) |
                ((long) (bKey[1] & 0xFF) << 8) |
                (long) (bKey[0] & 0xFF);
        return res & 0xffffffffL;
    }
}

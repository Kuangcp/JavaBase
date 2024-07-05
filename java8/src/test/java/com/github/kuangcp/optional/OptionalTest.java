package com.github.kuangcp.optional;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Optional;

/**
 * @author kuangcp on 3/5/19-5:24 PM
 */
@Slf4j
public class OptionalTest {

    private Optional<String> getId() {
        return Optional.empty();
    }

    private Optional<Integer> getCount(String id) {
        log.info("id={}", id);
        return Optional.of(2);
    }

    @Test
    public void testFlatMap() {
        Optional<Integer> result = getId().flatMap(this::getCount);

        assert !result.isPresent();
    }

    @Test
    public void testFlatMapWithNull() {
        Optional<Object> result = getId().flatMap(s -> null);
        System.out.println(result);
        assert !result.isPresent();
    }
}

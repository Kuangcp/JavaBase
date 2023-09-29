package com.github.kuangcp.queue.use.blocking;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author kuangcp on 2019-04-19 12:41 AM
 */
@Slf4j
public class VeterinarianTest {

    @Test
    public void testRun() throws Exception {
        BlockingQueue<Appointment<Pet>> lists = new LinkedBlockingQueue<>();
        lists.add(new Appointment<>(new Cat("1")));
        lists.add(new Appointment<>(new Cat("2")));
        lists.add(new Appointment<>(new Dog("1")));
        lists.add(new Appointment<>(new Dog("2")));

        Veterinarian veterinarian = new Veterinarian(lists, 2000);
        veterinarian.text = "Veterinarian 1";
        Veterinarian veterinarian2 = new Veterinarian(lists, 1000);
        veterinarian2.text = "Veterinarian 2";

        veterinarian.start();
        veterinarian2.start();

        veterinarian.join();
        veterinarian2.join();
    }
}
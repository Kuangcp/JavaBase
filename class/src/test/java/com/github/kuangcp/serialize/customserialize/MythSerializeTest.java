package com.github.kuangcp.serialize.customserialize;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author kuangcp on 2019-04-21 11:31 AM
 */
public class MythSerializeTest {


    @Test
    public void test() {
        MythSerialize<Myth> mythSerialize = new MythSerialize<>();
        Myth domain = new Myth();
        domain.setName("myth");
        domain.setPhone("121212121");
        domain.setTest(90909090L);
        ByteArrayOutputStream out = mythSerialize.out(domain);

        assert Objects.nonNull(out);

        Myth result = mythSerialize.in(Myth.class, new ByteArrayInputStream(out.toByteArray()));
        System.out.println(result.toString());

        assertThat(result.getName(), equalTo(domain.getName()));
    }
}

package com.github.kuangcp.reflects;

import net.sf.cglib.beans.BeanCopier;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="https://github.com/kuangcp">Github</a>
 * 2023-11-14 19:49
 */
public class RecordCopyTest {

    @Test
    public void testBeanCopier() throws Exception {
        BeanCopier copier = BeanCopier.create(BeanA.class, BeanB.class, false);
        for (int i = 0; i < 10000; i++) {
            BeanA a = new BeanA();
            a.setDocName("doc");
            BeanB b = new BeanB();
            copier.copy(a, b, null);
            assertThat(b.getDocName(), equalTo(a.getDocName()));
        }
    }

    @Test
    public void testUtil() throws Exception {

    }

}

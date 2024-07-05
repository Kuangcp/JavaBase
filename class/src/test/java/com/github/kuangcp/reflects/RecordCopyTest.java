package com.github.kuangcp.reflects;

import com.hellokaton.blade.Blade;
import net.sf.cglib.beans.BeanCopier;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="https://github.com/kuangcp">Github</a>
 * 2023-11-14 19:49
 */
public class RecordCopyTest {

    private static final BeanCopier copier = BeanCopier.create(BeanA.class, BeanB.class, false);

    @Test
    public void testBeanCopier() throws Exception {
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
        BeanUtil.fieldMap(BeanA.class);
        BeanUtil.fieldMap(BeanB.class);

        for (int i = 0; i < 10000; i++) {
            BeanA a = new BeanA();
            a.setDocName("doc");
            BeanB b = new BeanB();
            BeanUtil.copyProperties(a, b, false);
            assertThat(b.getDocName(), equalTo(a.getDocName()));
        }
    }

    public static void main(String[] args) {
        BeanUtil.fieldMap(BeanA.class);
        BeanUtil.fieldMap(BeanB.class);

        Blade.create()
                .listen(8745)
                .get("/", ctx -> ctx.text("Hello Blade"))
                .get("/u", ctx -> {
                    Optional<String> total = ctx.request().query("total");
                    ctx.text(utilHandle(total.map(Integer::parseInt).orElse(1)));
                })
                .get("/b", ctx -> {
                    Optional<String> total = ctx.request().query("total");
                    ctx.text(copyHandle(total.map(Integer::parseInt).orElse(1)));
                })
                .start(RecordCopyTest.class, args);
    }

    static String copyHandle(int total) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < total; i++) {
            BeanA a = new BeanA();
            a.setDocName("doc");
            BeanB b = new BeanB();
            copier.copy(a, b, null);
        }
        long end = System.currentTimeMillis();

        long gv = end - start;
        return gv + "";
    }

    static String utilHandle(int total) {
        long start = System.currentTimeMillis();
        BeanA a = new BeanA();
        a.setDocName("doc");
        a.setApplicantName("xxx");
        a.setApplyId(123456L);
        a.setDataExpire(new Date());
        a.setDocAuthorization(Arrays.asList(2, 4));

        for (int i = 0; i < total; i++) {
            BeanB b = new BeanB();
            BeanUtil.copyProperties(a, b, false);
        }
        long end = System.currentTimeMillis();

        long gv = end - start;
        return gv + "";
    }


}

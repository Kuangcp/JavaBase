package com.github.kuangcp.reflects;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import net.sf.cglib.beans.BeanCopier;
import org.junit.Test;
import sun.net.httpserver.HttpServerImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Date;

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

    public static void main(String[] args) throws Exception {
        BeanUtil.fieldMap(BeanA.class);
        BeanUtil.fieldMap(BeanB.class);

        HttpServer s = HttpServerImpl.create(new InetSocketAddress(8745), 1000);
        s.createContext("/u", RecordCopyTest::utilHandle);
        s.createContext("/b", RecordCopyTest::copyHandle);

        s.start();
    }

    static void copyHandle(HttpExchange v) throws IOException {
        long start = System.currentTimeMillis();
        String query = v.getRequestURI().getPath();
        int ix = query.lastIndexOf("/");
        String total = query.substring(ix + 1);
        for (int i = 0; i < Integer.parseInt(total); i++) {
            BeanA a = new BeanA();
            a.setDocName("doc");
            BeanB b = new BeanB();
            copier.copy(a, b, null);
        }
        long end = System.currentTimeMillis();

        long gv = end - start;
        String res = gv + "";
        v.sendResponseHeaders(200, res.length());
        v.getResponseBody().write(res.getBytes());
        v.getResponseBody().close();
    }

    static void utilHandle(HttpExchange v) throws IOException {
        long start = System.currentTimeMillis();
        String query = v.getRequestURI().getPath();
        int ix = query.lastIndexOf("/");
        String total = query.substring(ix + 1);
        BeanA a = new BeanA();
        a.setDocName("doc");
        a.setApplicantName("xxx");
        a.setApplyId(123456L);
        a.setDataExpire(new Date());
        a.setDocAuthorization(Arrays.asList(2,4));

        for (int i = 0; i < Integer.parseInt(total); i++) {
            BeanB b = new BeanB();
            BeanUtil.copyProperties(a, b, false);
        }
        long end = System.currentTimeMillis();

        long gv = end - start;
        String res = gv + "";
        v.sendResponseHeaders(200, res.length());
        v.getResponseBody().write(res.getBytes());
        v.getResponseBody().close();
    }


}

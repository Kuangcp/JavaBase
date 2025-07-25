package jvm.oom;

import org.junit.Test;

/**
 * @author Kuangcp
 * 2025-05-19 21:36
 */
public class DirectMemoryGlibcGrowTest {

    @Test
    public void testFragment() throws Exception {
        DirectMemoryGlibcGrow.fragment();
        Thread.currentThread().join();
    }

    /**
     * pmap -x 259296 | jvm-tool -s 查看块数量
     * 注意，IDE启动，则是使用全部的核心，最大thread arena数量是 cpu核数*8
     * 可复制IDE第一行命令，创建shell脚本，删除-agentlib:jdwp的参数，命令前添加 taskset -c 0,1 来限制CPU核心数，可以看到数量就变成17了 1+8*2, 1是 main arena
     * <p>
     * 替换为jemalloc export LD_PRELOAD=/usr/lib/libjemalloc.so，固定37M块并行分配的情况 两种malloc都是RSS内存在1-7G内存浮动，glibc可以看到17个arena
     * 所以这个测试场景不能充分展示 大对象导致 大量thread arena 占用
     */
    @Test
    public void testLoopFragment() throws Exception {
        DirectMemoryGlibcGrow.loopFragment();
        Thread.currentThread().join();
    }
}

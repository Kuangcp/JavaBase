package com.github.kuangcp.juc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kuangcp
 * 2025-11-13 22:02
 */
@Slf4j
public class PhaseTest {

    /**
     * 演示 Phaser 协调多个线程的多个计算任务
     * 场景：多个线程并行执行三个阶段的计算（平方、立方、求和）
     * 要求：每个阶段完成后，所有线程等待其他线程，然后按阶段依次打印结果
     */
    @Test
    public void testBasic() throws Exception {
        int threadCount = 4; // 线程数量
        int phaseCount = 3;  // 阶段数量

        // 创建 Phaser，注册主线程和所有工作线程
        Phaser phaser = new Phaser(1) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                // 每个阶段完成时的回调
                log.info("\n========== 阶段 {} 完成 ==========", phase + 1);
                return phase >= phaseCount - 1; // 如果完成所有阶段，返回true终止
            }
        };

        // 存储每个线程的结果
        List<ThreadResult> results = new ArrayList<>();

        // 创建并启动工作线程
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            ThreadResult result = new ThreadResult(threadId);
            results.add(result);

            phaser.register(); // 注册线程到 Phaser

            Thread thread = new Thread(() -> {
                try {
                    // 阶段1：计算平方
                    int value = ThreadLocalRandom.current().nextInt(1, 10);
                    int square = value * value;
                    result.setPhase1Value(value);
                    result.setPhase1Result(square);
                    log.info("线程-{}: 阶段1完成，输入={}，平方={}", threadId, value, square);
                    phaser.arriveAndAwaitAdvance(); // 等待所有线程完成阶段1

                    // 阶段2：计算立方
                    int cube = value * value * value;
                    result.setPhase2Result(cube);
                    log.info("线程-{}: 阶段2完成，立方={}", threadId, cube);
                    phaser.arriveAndAwaitAdvance(); // 等待所有线程完成阶段2

                    // 阶段3：计算总和
                    int sum = square + cube;
                    result.setPhase3Result(sum);
                    log.info("线程-{}: 阶段3完成，总和={}", threadId, sum);
                    phaser.arriveAndAwaitAdvance(); // 等待所有线程完成阶段3

                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    phaser.arriveAndDeregister(); // 完成并注销
                }
            });

            thread.start();
        }

        // 主线程等待所有阶段完成
        for (int phase = 0; phase < phaseCount; phase++) {
            phaser.arriveAndAwaitAdvance(); // 等待当前阶段完成
            log.info("\n--- 阶段 {} 结果汇总 ---", phase + 1);
            printPhaseResults(results, phase + 1);
        }

        // 等待所有线程完成
        phaser.arriveAndDeregister();

        // 打印最终汇总
        log.info("\n========== 最终结果汇总 ==========");
        for (ThreadResult result : results) {
            log.info("线程-{}: 输入={}, 平方={}, 立方={}, 总和={}",
                    result.threadId,
                    result.phase1Value,
                    result.phase1Result,
                    result.phase2Result,
                    result.phase3Result);
        }
    }

    private void printPhaseResults(List<ThreadResult> results, int phase) {
        for (ThreadResult result : results) {
            switch (phase) {
                case 1:
                    log.info("  线程-{}: 平方={}", result.threadId, result.phase1Result);
                    break;
                case 2:
                    log.info("  线程-{}: 立方={}", result.threadId, result.phase2Result);
                    break;
                case 3:
                    log.info("  线程-{}: 总和={}", result.threadId, result.phase3Result);
                    break;
            }
        }
    }

    /**
     * 演示并行执行但按依赖顺序输出的场景
     * 场景：4个任务 A、B、C、D 可以并行执行
     * 但输出结果时：D依赖C，C依赖B，B依赖A
     * 即输出顺序必须是：A -> B -> C -> D
     */
    @Test
    public void testDependentOutput() throws Exception {
        // 创建 Phaser，用于控制输出顺序
        // 4个阶段：phase 0(A输出) -> phase 1(B输出) -> phase 2(C输出) -> phase 3(D输出)
        Phaser outputPhaser = new Phaser(1) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                log.info("========== 输出阶段 {} 完成 ==========", phase + 1);
                return phase >= 3; // 完成所有4个输出阶段后终止
            }
        };

        // 任务结果存储
        TaskResult resultA = new TaskResult("A");
        TaskResult resultB = new TaskResult("B");
        TaskResult resultC = new TaskResult("C");
        TaskResult resultD = new TaskResult("D");

        log.info("========== 开始并行执行任务 ==========");

        // 任务A：独立执行，完成后可以立即输出（phase 0）
        outputPhaser.register();
        Thread taskA = new Thread(() -> {
            try {
                // 模拟任务执行（随机耗时）
                int duration = ThreadLocalRandom.current().nextInt(500, 2000);
                log.info("任务A: 开始执行，预计耗时 {}ms", duration);
                Thread.sleep(duration);
                resultA.setResult("任务A的结果: " + duration);
                log.info("任务A: 执行完成");

                // 到达输出阶段0，等待其他任务（如果有）也到达，然后输出
                outputPhaser.arriveAndAwaitAdvance();
                log.info(">>> 输出任务A结果: {}", resultA.getResult());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("任务A被中断", e);
            } finally {
                outputPhaser.arriveAndDeregister();
            }
        });

        // 任务B：独立执行，但输出需要等待A（phase 1）
        outputPhaser.register();
        Thread taskB = new Thread(() -> {
            try {
                int duration = ThreadLocalRandom.current().nextInt(500, 2000);
                log.info("任务B: 开始执行，预计耗时 {}ms", duration);
                Thread.sleep(duration);
                resultB.setResult("任务B的结果: " + duration);
                log.info("任务B: 执行完成");

                // 等待A输出完成（phase 0），然后到达phase 1并输出
                outputPhaser.arriveAndAwaitAdvance();
                log.info(">>> 输出任务B结果: {} (依赖A)", resultB.getResult());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("任务B被中断", e);
            } finally {
                outputPhaser.arriveAndDeregister();
            }
        });

        // 任务C：独立执行，但输出需要等待B（phase 2）
        outputPhaser.register();
        Thread taskC = new Thread(() -> {
            try {
                int duration = ThreadLocalRandom.current().nextInt(500, 2000);
                log.info("任务C: 开始执行，预计耗时 {}ms", duration);
                Thread.sleep(duration);
                resultC.setResult("任务C的结果: " + duration);
                log.info("任务C: 执行完成");

                // 等待B输出完成（phase 1），然后到达phase 2并输出
                outputPhaser.arriveAndAwaitAdvance();
                log.info(">>> 输出任务C结果: {} (依赖B)", resultC.getResult());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("任务C被中断", e);
            } finally {
                outputPhaser.arriveAndDeregister();
            }
        });

        // 任务D：独立执行，但输出需要等待C（phase 3）
        outputPhaser.register();
        Thread taskD = new Thread(() -> {
            try {
                int duration = ThreadLocalRandom.current().nextInt(500, 2000);
                log.info("任务D: 开始执行，预计耗时 {}ms", duration);
                Thread.sleep(duration);
                resultD.setResult("任务D的结果: " + duration);
                log.info("任务D: 执行完成");

                // 等待C输出完成（phase 2），然后到达phase 3并输出
                outputPhaser.arriveAndAwaitAdvance();
                log.info(">>> 输出任务D结果: {} (依赖C)", resultD.getResult());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("任务D被中断", e);
            } finally {
                outputPhaser.arriveAndDeregister();
            }
        });

        // 启动所有任务（并行执行）
        long startTime = System.currentTimeMillis();
        taskA.start();
        taskB.start();
        taskC.start();
        taskD.start();

        // 主线程等待所有输出阶段完成
        for (int phase = 0; phase < 4; phase++) {
            outputPhaser.arriveAndAwaitAdvance();
        }

        // 等待所有线程完成
        outputPhaser.arriveAndDeregister();

        taskA.join();
        taskB.join();
        taskC.join();
        taskD.join();

        long totalTime = System.currentTimeMillis() - startTime;
        log.info("\n========== 所有任务完成 ==========");
        log.info("总耗时: {}ms", totalTime);
        log.info("注意：虽然任务并行执行，但输出顺序严格按照 A -> B -> C -> D");
    }

    // 任务结果封装类
    static class TaskResult {
        private final String taskName;
        private String result;

        TaskResult(String taskName) {
            this.taskName = taskName;
        }

        void setResult(String result) {
            this.result = result;
        }

        String getResult() {
            return result;
        }

        String getTaskName() {
            return taskName;
        }
    }

    // 线程结果封装类
    static class ThreadResult {
        final int threadId;
        int phase1Value;
        int phase1Result;
        int phase2Result;
        int phase3Result;

        ThreadResult(int threadId) {
            this.threadId = threadId;
        }

        void setPhase1Value(int value) {
            this.phase1Value = value;
        }

        void setPhase1Result(int result) {
            this.phase1Result = result;
        }

        void setPhase2Result(int result) {
            this.phase2Result = result;
        }

        void setPhase3Result(int result) {
            this.phase3Result = result;
        }
    }
}

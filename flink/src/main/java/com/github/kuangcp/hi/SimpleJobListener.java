package com.github.kuangcp.hi;

import static org.apache.flink.runtime.jobgraph.JobStatus.FINISHED;

import org.apache.flink.api.common.JobID;
import org.apache.flink.runtime.executiongraph.ExecutionGraph;
import org.apache.flink.runtime.executiongraph.JobStatusListener;
import org.apache.flink.runtime.jobgraph.JobStatus;

/**
 * 如果该对象能注入到 下面类的属性上, 问题就简单一些了
 *
 * @author https://github.com/kuangcp on 2019-07-08 11:22
 * @see ExecutionGraph jobStatusListeners
 */
@Deprecated
public class SimpleJobListener implements JobStatusListener {

  @Override
  public void jobStatusChanges(JobID jobId, JobStatus newJobStatus, long timestamp,
      Throwable error) {
    System.out.println(jobId);

    if (FINISHED.equals(newJobStatus)) {
      System.out.println("complete");
    }
  }
}

package com.lsm.batch.dormantbatch;

import java.time.LocalDateTime;
import java.util.Objects;

public class Job {

	private final Tasklet tasklet;
	private final JobExecutionListener jobExecutionListener;

	public Job(Tasklet tasklet) {
		this(tasklet, null);
	}

	public Job(Tasklet tasklet, JobExecutionListener jobExecutionListener) {
		this.tasklet = tasklet;
		this.jobExecutionListener = Objects.requireNonNullElseGet(jobExecutionListener,
			() -> new JobExecutionListener() {
				@Override
				public void beforeJob(JobExecution jobExecution) {

				}

				@Override
				public void afterJob(JobExecution jobExecution) {

				}
			});

	}

	public JobExecution execute() {

		JobExecution jobExecution = new JobExecution();
		jobExecution.setStatus(BatchStatus.STARTING);
		jobExecution.setStartTime(LocalDateTime.now());

		jobExecutionListener.beforeJob(jobExecution);

		try {
			tasklet.execute();
			jobExecution.setStatus(BatchStatus.COMPLETED);
		} catch (Exception e) {
			jobExecution.setStatus(BatchStatus.FAILED);
		}

		jobExecution.setEndTime(LocalDateTime.now());

		jobExecutionListener.afterJob(jobExecution);
		return jobExecution;

	}
}

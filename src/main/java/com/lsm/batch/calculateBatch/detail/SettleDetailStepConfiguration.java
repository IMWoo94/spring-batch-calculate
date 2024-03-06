package com.lsm.batch.calculateBatch.detail;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.lsm.batch.calculateBatch.domain.ApiOrder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SettleDetailStepConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;

	@Bean
	public Job preSettleDetailJob(
		Step preSettleDetailStep,
		Step contextShareStep
	) {
		return new JobBuilder("preSettleDetailJob", jobRepository)
			.start(preSettleDetailStep)
			.next(contextShareStep)
			.incrementer(new RunIdIncrementer())
			.build();
	}

	// 첫번째 Step : 일자의 파일을 고객 + 서비스 별로 집계를 하여 Execution Context 안에 넣는다.
	// Key[ A, 1 ] 13 호출

	// 두번째 Step : 집계된 Execution Context 데이터를 가지고 DB 에 Write 한다.

	@Bean
	public Step contextShareStep(
		ItemReader<Key> testReader
	) {
		return new StepBuilder("contextShareStep", jobRepository)
			.<Key, Key>chunk(5000, platformTransactionManager)
			.reader(testReader)
			.writer(new ItemWriter<Key>() {
				@Override
				public void write(Chunk<? extends Key> chunk) throws Exception {

				}
			})
			.build();
	}

	@Bean
	public Step preSettleDetailStep(
		FlatFileItemReader<ApiOrder> preSettleDetailReader,
		PreSettleDetailWriter preSettleDetailWriter,
		ExecutionContextPromotionListener executionContextPromotionListener
	) {
		return new StepBuilder("preSettleDetailStep", jobRepository)
			.<ApiOrder, Key>chunk(5000, platformTransactionManager)
			.reader(preSettleDetailReader)
			.processor(new PreSettleDetailProcessor())
			.writer(preSettleDetailWriter)
			.listener(executionContextPromotionListener)
			.build();
	}

	@Bean
	public ExecutionContextPromotionListener executionContextPromotionListener() {
		ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();
		executionContextPromotionListener.setKeys(new String[] {"snapshots"});
		return executionContextPromotionListener;
	}

	@Bean
	@StepScope
	public FlatFileItemReader<ApiOrder> preSettleDetailReader(
		@Value("#{jobParameters['targetDate']}") String targetDate
	) {
		String fileName = targetDate + "_api_orders.csv";

		return new FlatFileItemReaderBuilder<ApiOrder>()
			.name("preSettleDetailReader")
			.resource(new ClassPathResource("/datas/" + fileName))
			.linesToSkip(1)
			.delimited()
			.names("id", "customerId", "url", "state", "createdAt")
			.targetType(ApiOrder.class)
			.build();

	}
}

package org.logly.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.logly.domain.company.Company;
import org.logly.domain.company.CompanyInitBatchRecord;
import org.logly.domain.company.CompanyRepository;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CompanyBatchConfig {

    private final CompanyRepository companyRepository;

    @Bean
    public Job companyJob(JobRepository jobRepository, Step companyStep) {
        return new JobBuilder("InitCompanyJob", jobRepository)
                .start(companyStep)
                .build();
    }

    @Bean
    public Step companyStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("InitCompnayStep", jobRepository)
                .<CompanyInitBatchRecord, Company>chunk(20, transactionManager)
                .reader(companyItemReader())
                .processor(companyItemProcessor())
                .writer(companyItemWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<CompanyInitBatchRecord> companyItemReader() {
        FlatFileItemReader<CompanyInitBatchRecord> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("companies.csv"));
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("name", "blogUrl");

        BeanWrapperFieldSetMapper<CompanyInitBatchRecord> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(CompanyInitBatchRecord.class);

        DefaultLineMapper<CompanyInitBatchRecord> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public ItemProcessor<CompanyInitBatchRecord, Company> companyItemProcessor() {
        return CompanyInitBatchRecord::toCompany;
    }

    @Bean
    public ItemWriter<Company> companyItemWriter() {
        return items -> {
            items.forEach(companyRepository::save);
            log.info("saved {} companies", items.size());
        };
    }
}

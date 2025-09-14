package org.logly.batch.validation;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

@Component
public class BlogPageJobParameterValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        Long startPage = parameters.getLong("startPage");
        Long endPage = parameters.getLong("endPage");

        if (startPage == null) {
            throw new JobParametersInvalidException("startPage는 필수 파라미터입니다.");
        }

        if (endPage == null) {
            throw new JobParametersInvalidException("endPage는 필수 파라미터입니다.");
        }

        if (startPage < 0) {
            throw new JobParametersInvalidException("startPage는 0 이상이어야 합니다.");
        }

        if (endPage < 0) {
            throw new JobParametersInvalidException("startPage는 0 이상이어야 합니다.");
        }

        if (startPage > endPage) {
            throw new JobParametersInvalidException("startPage는 endPage보다 작거나 같아야 합니다.");
        }
    }
}

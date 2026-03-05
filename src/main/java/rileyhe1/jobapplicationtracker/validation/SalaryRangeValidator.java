package rileyhe1.jobapplicationtracker.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import rileyhe1.jobapplicationtracker.dto.joblisting.JobListingRequest;

public class SalaryRangeValidator implements ConstraintValidator<ValidSalaryRange, JobListingRequest>
{
    @Override
    public boolean isValid(JobListingRequest jobListingRequest, ConstraintValidatorContext constraintValidatorContext)
    {
        if(jobListingRequest.getSalaryMin() == null || jobListingRequest.getSalaryMax() == null) return true;
        return jobListingRequest.getSalaryMin() <= jobListingRequest.getSalaryMax();
    }
}

package com.bulbul.spring.quartz.validator;

import com.bulbul.spring.quartz.annotation.ValidCronExpression;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.quartz.CronExpression;

import java.text.ParseException;

public class CronExpressionValidator implements ConstraintValidator<ValidCronExpression, String> {

    @Override
    public boolean isValid(String cronExpression, ConstraintValidatorContext context) {
        if (cronExpression == null || cronExpression.isEmpty()) return false;
        try {
            new CronExpression(cronExpression); // from org.quartz.CronExpression
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}

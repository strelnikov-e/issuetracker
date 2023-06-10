package com.strelnikov.issuetracker.utils;

import com.strelnikov.issuetracker.entity.IssuePriority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class IssuePriorityConverter implements AttributeConverter<IssuePriority, String> {

    @Override
    public String convertToDatabaseColumn(IssuePriority issueStatus) {
        if (issueStatus == null) {
            return null;
        }
        return issueStatus.getCode();
    }

    @Override
    public IssuePriority convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(IssuePriority.values())
                .filter(s -> s.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }


}

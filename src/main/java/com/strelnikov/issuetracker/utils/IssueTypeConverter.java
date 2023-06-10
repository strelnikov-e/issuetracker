package com.strelnikov.issuetracker.utils;

import com.strelnikov.issuetracker.entity.IssueType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class IssueTypeConverter implements AttributeConverter<IssueType, String> {

    @Override
    public String convertToDatabaseColumn(IssueType issueStatus) {
        if (issueStatus == null) {
            return null;
        }
        return issueStatus.getCode();
    }

    @Override
    public IssueType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(IssueType.values())
                .filter(s -> s.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }


}

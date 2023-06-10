package com.strelnikov.issuetracker.utils;

import com.strelnikov.issuetracker.entity.IssueStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class IssueStatusConverter implements AttributeConverter<IssueStatus, String> {

    @Override
    public String convertToDatabaseColumn(IssueStatus issueStatus) {
        if (issueStatus == null) {
            return null;
        }
        return issueStatus.getCode();
    }

    @Override
    public IssueStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(IssueStatus.values())
                .filter(s -> s.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }


}

package com.tellcarl.domain;

import lombok.Value;

import java.util.List;

@Value
public class DreamReport
{
    String                                               dreamText;
    String                                               dreamTextWithMarkup;
    List<com.amazonaws.services.comprehend.model.Entity> awsEntities;
//    List<com.google.cloud.language.v1.Entity>            googleEntities;
    Work                                                 work;
    Boolean                                              isTruncated;

    @Value
    public static class Work
    {
        List<DreamSentence> mainSentences;
    }
}

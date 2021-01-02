package com.tellcarl.domain;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
public class DreamSentence
{
    String text;
    @EqualsAndHashCode.Exclude Float  relevance;
}

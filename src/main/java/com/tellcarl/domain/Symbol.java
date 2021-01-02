package com.tellcarl.domain;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class Symbol
{
    String     lemma;
    SymbolType symbolType;
    @EqualsAndHashCode.Exclude
    Boolean isUniversal;
    @EqualsAndHashCode.Exclude
    String  explanation;
    @EqualsAndHashCode.Exclude
    String  imgFilename;
}

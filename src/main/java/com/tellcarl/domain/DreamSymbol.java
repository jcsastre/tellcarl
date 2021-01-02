package com.tellcarl.domain;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
public class DreamSymbol
{
    String word;
    Symbol symbol;
    String adj;
    String provider;
    String providerType;
}

package com.tellcarl.domain;

import com.google.cloud.language.v1.PartOfSpeech.Tag;

public enum SymbolType
{
    PERSON,
    OBJECT,
    CHARACTER,
    PLACE,
    ACTION,
    OTHER;

    public static SymbolType of( Tag tag )
    {
        if ( tag == Tag.VERB )
        {
            return SymbolType.ACTION;
        }

        return SymbolType.OTHER;
    }
}

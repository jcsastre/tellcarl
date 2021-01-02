package com.tellcarl.domain;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class Dream
{
    Dreamer dreamer;
    String dream;
    LocalDateTime dateTime;
    Boolean processed;
}

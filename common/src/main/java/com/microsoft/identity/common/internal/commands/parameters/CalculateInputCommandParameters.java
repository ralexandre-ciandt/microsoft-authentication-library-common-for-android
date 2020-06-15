package com.microsoft.identity.common.internal.commands.parameters;

import com.google.gson.annotations.Expose;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class CalculateInputCommandParameters extends CommandParameters {

    @Expose()
    private int num1;

    @Expose()
    private int num2;

    @Expose()
    private char operation;
}

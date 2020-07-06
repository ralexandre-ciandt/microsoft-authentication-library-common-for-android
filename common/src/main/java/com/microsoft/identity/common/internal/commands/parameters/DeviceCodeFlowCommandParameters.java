package com.microsoft.identity.common.internal.commands.parameters;

import com.google.gson.annotations.Expose;

import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class DeviceCodeFlowCommandParameters extends CommandParameters {
    //@Expose()
    //private DeviceCodeFlowCallback callback;

    @Expose()
    private Set<String> scopes;
}

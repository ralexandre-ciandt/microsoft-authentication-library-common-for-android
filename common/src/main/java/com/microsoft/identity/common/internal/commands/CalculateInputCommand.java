package com.microsoft.identity.common.internal.commands;

import androidx.annotation.NonNull;

import com.microsoft.identity.common.internal.commands.parameters.CommandParameters;
import com.microsoft.identity.common.internal.controllers.BaseController;

public class CalculateInputCommand extends BaseCommand<String> {

    public CalculateInputCommand(@NonNull CommandParameters parameters,
                                @NonNull BaseController controller,
                                @NonNull CommandCallback callback,
                                @NonNull String publicApiId) {
        super(parameters, controller, callback, publicApiId);
    }

    @Override
    public String execute() throws Exception {
        // TODO: implement
        String result = "REACHED";
        return result;
    }

    @Override
    public boolean isEligibleForEstsTelemetry() {
        return false;
    }
}

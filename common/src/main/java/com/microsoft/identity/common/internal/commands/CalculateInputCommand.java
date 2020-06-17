package com.microsoft.identity.common.internal.commands;

import androidx.annotation.NonNull;

import com.microsoft.identity.common.internal.commands.parameters.CalculateInputCommandParameters;
import com.microsoft.identity.common.internal.commands.parameters.CommandParameters;
import com.microsoft.identity.common.internal.commands.parameters.RemoveAccountCommandParameters;
import com.microsoft.identity.common.internal.controllers.BaseController;

import java.util.List;

public class CalculateInputCommand extends BaseCommand<String> {

    public CalculateInputCommand(@NonNull CalculateInputCommandParameters parameters,
                                @NonNull BaseController controller,
                                @NonNull CommandCallback callback,
                                @NonNull String publicApiId) {
        super(parameters, controller, callback, publicApiId);
    }

    @Override
    public String execute() throws Exception {
        BaseController controller = getDefaultController();
        CalculateInputCommandParameters parameters = (CalculateInputCommandParameters) getParameters();
        return controller.calculateInput(parameters);
    }

    @Override
    public boolean isEligibleForEstsTelemetry() {
        return false;
    }
}

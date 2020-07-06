package com.microsoft.identity.common.internal.commands;

import androidx.annotation.NonNull;
import com.microsoft.identity.common.internal.commands.parameters.DeviceCodeFlowCommandParameters;
import com.microsoft.identity.common.internal.controllers.BaseController;
import com.microsoft.identity.common.internal.net.HttpResponse;
import com.microsoft.identity.common.internal.result.AcquireTokenResult;

import java.util.HashMap;

public class DeviceCodeFlowCommand extends BaseCommand<AcquireTokenResult> {
    /**
     * Relavent error codes and messages
     */
    public final static String AUTH_DECLINED_CODE = "authorization_declined";
    public final static String AUTH_DECLINED_MSG = "The end user has denied the authorization request. Re-run the Device Code Flow Protocol with another user.";

    public final static String EXPIRED_TOKEN_CODE = "expired_token";
    public final static String EXPIRED_TOKEN_MSG = "The token has expired, therefore authentication is no longer possible with this flow attempt. Re-run the Device Code Flow Protocol to try again.";

    // Other errors go here

    public DeviceCodeFlowCommand(@NonNull DeviceCodeFlowCommandParameters parameters,
                                 @NonNull BaseController controller,
                                 @NonNull CommandCallback callback,
                                 @NonNull String publicApiId) {
        super(parameters, controller, callback, publicApiId);
    }


    @Override
    public AcquireTokenResult execute() throws Exception {
        // Update logger

        // Get the controller used to execute the command
        BaseController controller = getDefaultController();

        // Fetch the parameters
        DeviceCodeFlowCommandParameters commandParameters = (DeviceCodeFlowCommandParameters) getParameters();

        // Call dcfAuthRequest in LocalMsalController to get user_code and more
        HttpResponse authResponse = controller.dcfAuthRequest(commandParameters);

        // Populate tokenParams HashMap from authResponse and commandParams
        HashMap<String, String> tokenParams = populateTokenParams(authResponse, commandParameters);

        // Display verification_uri, user_code, and message through callback
//        commandParameters.callback.getUserCode(
//                tokenParams.get("verification_uri"),
//                tokenParams.get("user_code"),
//                tokenParams.get("message")
//        );

        // Call dcfTokenRequest in LocalMsalController to get token
        return controller.dcfTokenRequest(commandParameters, tokenParams);
    }

    private HashMap<String, String> populateTokenParams(@NonNull HttpResponse authResponse, DeviceCodeFlowCommandParameters cParams) {
        // Use the response body from auth request to populate tokenParams
        // Use cParams for client_id
        return null;
    }

    @Override
    public boolean isEligibleForEstsTelemetry() {
        return true;
    }
}

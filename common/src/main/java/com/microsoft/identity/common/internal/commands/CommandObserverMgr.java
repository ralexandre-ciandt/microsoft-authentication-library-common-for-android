//  Copyright (c) Microsoft Corporation.
//  All rights reserved.
//
//  This code is licensed under the MIT License.
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files(the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions :
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.
package com.microsoft.identity.common.internal.commands;

import android.os.Handler;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.microsoft.identity.common.internal.controllers.CommandResult;
import com.microsoft.identity.common.internal.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.microsoft.identity.common.internal.controllers.CommandDispatcher.returnCommandResult;

public class CommandObserverMgr {

    /**
     * Tag for logging.
     */
    private static final String LOG_TAG = CommandObserverMgr.class.getSimpleName();

    /**
     * Our Singleton.
     */
    private static CommandObserverMgr INSTANCE;

    /**
     * A list of Observers to notify when this job finishes.
     */
    private final Map<BaseCommand<?>, List<Pair<CommandCallback<?, ?>, String>>> commandObservers = new HashMap<>();

    public static synchronized CommandObserverMgr getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new CommandObserverMgr();
        }

        return INSTANCE;
    }

    public synchronized void addObserver(@NonNull final BaseCommand<?> command) {
        if (null == commandObservers.get(command)) {
            commandObservers.put(command, new ArrayList<Pair<CommandCallback<?, ?>, String>>());
        }

        final Pair<CommandCallback<?, ?>, String> cmdPair =
                new Pair<CommandCallback<?, ?>, String>(
                        command.getCallback(),
                        command.getParameters().getCorrelationId()
                );

        commandObservers.get(command).add(cmdPair);
    }

    public synchronized void onCommandCompleted(@NonNull final BaseCommand<?> command,
                                                @NonNull final CommandResult result,
                                                @NonNull final Handler handler) {
        final List<Pair<CommandCallback<?, ?>, String>> subs = commandObservers.get(command);

        if (null == subs || subs.isEmpty()) {
            // Just 1 observer, simply send the result to them
            returnCommandResult(command, result, handler);
        } else {
            for (Pair<CommandCallback<?, ?>, String> subPair : subs) {
                final CommandCallback callback = subPair.first;

                // This request began with the commandParam correlation_id
                final String originalCorrelationId = command.getParameters().getCorrelationId();

                // The correlation_id this callback expects
                final String callbackSpecificCorrelationId = subPair.second;

                if (!originalCorrelationId.equals(callbackSpecificCorrelationId)) {
                    // Correlation Ids don't match -- different threads expecting different ids
                    Logger.warn(
                            LOG_TAG,
                            "Multiple threads waiting on this result."
                                    + "\n"
                                    + "Original correlation ID: " + originalCorrelationId
                                    + "\n"
                                    + "Dropped correlation ID: " + callbackSpecificCorrelationId,
                            null
                    );

                    // If we could deep-copy the result, update the correlation_id and return
                    // doesn't look like we have a good way to do that?
                    //setCorrelationIdOnResult(result, callbackSpecificCorrelationId);
                }

                returnCommandResult(command, result, handler);
            }
        }

        // We're done, remove the pair
        commandObservers.remove(command);
    }
}
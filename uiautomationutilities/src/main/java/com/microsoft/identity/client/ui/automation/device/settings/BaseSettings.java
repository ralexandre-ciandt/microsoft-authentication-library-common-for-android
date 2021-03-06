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
package com.microsoft.identity.client.ui.automation.device.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.uiautomator.UiObject;

import com.microsoft.identity.client.ui.automation.utils.UiAutomatorUtils;

import org.junit.Assert;

public abstract class BaseSettings implements ISettings {

    final static String SETTINGS_PKG = "com.android.settings";

    private static void launchIntent(@NonNull final String action) {
        final Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = new Intent(action);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivity(intent);
    }

    @Override
    public void launchDeviceAdminSettingsPage() {
        final Intent intent = new Intent();
        intent.setComponent(new ComponentName(
                SETTINGS_PKG,
                SETTINGS_PKG + ".DeviceAdminSettings")
        );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        final Context context = ApplicationProvider.getApplicationContext();
        context.startActivity(intent);

        final UiObject deviceAdminPage = UiAutomatorUtils.obtainUiObjectWithText("device admin");
        Assert.assertTrue("Device Admin Settings Page appears", deviceAdminPage.exists());
    }

    @Override
    public void launchAddAccountPage() {
        launchIntent(Settings.ACTION_ADD_ACCOUNT);
    }

    @Override
    public void launchAccountListPage() {
        launchIntent(Settings.ACTION_SYNC_SETTINGS);
    }

    @Override
    public void launchDateTimeSettingsPage() {
        launchIntent(Settings.ACTION_DATE_SETTINGS);
    }
}

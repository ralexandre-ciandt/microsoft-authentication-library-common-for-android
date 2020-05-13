package com.microsoft.identity.client.ui.automation.utils;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Assert;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.microsoft.identity.client.ui.automation.utils.CommonUtils.TIMEOUT;
import static com.microsoft.identity.client.ui.automation.utils.CommonUtils.getResourceId;
import static com.microsoft.identity.client.ui.automation.utils.CommonUtils.isStringPackageName;
import static com.microsoft.identity.client.ui.automation.utils.CommonUtils.launchApp;

public class PlayStoreUtils {

    final static String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";

    private static void searchAppOnGooglePlay(final String hint) {
        UiDevice device = UiDevice.getInstance(getInstrumentation());

        launchApp(GOOGLE_PLAY_PACKAGE_NAME);

        UiObject searchButton = device.findObject(new UiSelector().resourceId(
                getResourceId(GOOGLE_PLAY_PACKAGE_NAME, "search_bar_hint")
        ));
        try {
            searchButton.waitForExists(TIMEOUT);
            searchButton.click();
        } catch (UiObjectNotFoundException e) {
            Assert.fail(e.getMessage());
        }

        UiObject searchTextField = device.findObject(new UiSelector().resourceId(
                getResourceId(GOOGLE_PLAY_PACKAGE_NAME, "search_bar_text_input")
        ));
        try {
            searchTextField.waitForExists(TIMEOUT);
            searchTextField.setText(hint);
        } catch (UiObjectNotFoundException e) {
            Assert.fail(e.getMessage());
        }

        device.pressEnter();
    }

    private static void selectGooglePlayAppFromAppList(final String appName) throws UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject appIconInSearchResult = device.findObject(new UiSelector().resourceId(
                getResourceId(GOOGLE_PLAY_PACKAGE_NAME, "play_card")
        ).descriptionContains(appName));

        appIconInSearchResult.waitForExists(TIMEOUT);
        appIconInSearchResult.click();
    }

    private static void selectGooglePlayAppFromInstallBar(final String appName) throws UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject appInstallBar = device.findObject(new UiSelector().resourceId(
                getResourceId(GOOGLE_PLAY_PACKAGE_NAME, "install_bar")
        ));
        appInstallBar.waitForExists(TIMEOUT);
        appInstallBar.click();
    }

    private static void selectGooglePlayAppFromAppName(final String appName) {
        try {
            selectGooglePlayAppFromInstallBar(appName);
        } catch (UiObjectNotFoundException e) {
            try {
                selectGooglePlayAppFromAppList(appName);
            } catch (UiObjectNotFoundException ex) {
                Assert.fail(ex.getMessage());
            }
        }
    }

    private static void selectGooglePlayAppFromPackageNameOld(final String appName) {
        UiDevice device = UiDevice.getInstance(getInstrumentation());

        // we will just take the first app in the list
        UiObject appIconInSearchResult = device.findObject(new UiSelector().resourceId(
                getResourceId(GOOGLE_PLAY_PACKAGE_NAME, "bucket_items")
        ).index(0).childSelector(new UiSelector().resourceId(
                getResourceId(GOOGLE_PLAY_PACKAGE_NAME, "play_card")
        )));
        try {
            appIconInSearchResult.waitForExists(TIMEOUT);
            appIconInSearchResult.click();
        } catch (UiObjectNotFoundException e) {
            Assert.fail(e.getMessage());
        }
    }

    private static void selectGooglePlayAppFromPackageName(final String appName) {
        UiDevice device = UiDevice.getInstance(getInstrumentation());

        // we will just take the first app in the list
        UiObject appIconInSearchResult = device.findObject(new UiSelector().resourceId(
                getResourceId(GOOGLE_PLAY_PACKAGE_NAME, "bucket_items")
        ).childSelector(new UiSelector().textContains(appName)));
        try {
            appIconInSearchResult.waitForExists(TIMEOUT);
            appIconInSearchResult.click();
        } catch (UiObjectNotFoundException e) {
            Assert.fail(e.getMessage());
        }
    }

    private static void installOrOpenAppFromGooglePlay() {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject installButton = device.findObject(new UiSelector().resourceId(
                getResourceId(GOOGLE_PLAY_PACKAGE_NAME, "right_button")
        ));
        try {
            installButton.waitForExists(TIMEOUT);
            installButton.click();
        } catch (UiObjectNotFoundException e) {
            Assert.fail(e.getMessage());
        }

        UiObject openButton = device.findObject(new UiSelector().resourceId(
                getResourceId(GOOGLE_PLAY_PACKAGE_NAME, "right_button")
        ).textContains("Open").enabled(true));
        // if we see uninstall button, then we know that the installation is complete
        openButton.waitForExists(1000 * 300); // wait at least 5 mins for installation
    }

    public UiObject findInstallBar() {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject installBar = device.findObject(new UiSelector().resourceId(
                getResourceId(GOOGLE_PLAY_PACKAGE_NAME, "install_bar")
        ));

        installBar.waitForExists(TIMEOUT);
        return installBar;
    }

    public static void installApp(final String searchHint) {
        searchAppOnGooglePlay(searchHint);
        if (isStringPackageName(searchHint)) {
            selectGooglePlayAppFromPackageName(searchHint);
        } else {
            selectGooglePlayAppFromAppName(searchHint);
        }
        installOrOpenAppFromGooglePlay();
    }

}

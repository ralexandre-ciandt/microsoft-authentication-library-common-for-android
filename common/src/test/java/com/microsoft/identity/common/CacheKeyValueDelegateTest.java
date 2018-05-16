// Copyright (c) Microsoft Corporation.
// All rights reserved.
//
// This code is licensed under the MIT License.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files(the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions :
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
package com.microsoft.identity.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.microsoft.identity.common.internal.cache.CacheKeyValueDelegate;
import com.microsoft.identity.common.internal.cache.ICacheKeyValueDelegate;
import com.microsoft.identity.common.internal.dto.AccessToken;
import com.microsoft.identity.common.internal.dto.Credential;
import com.microsoft.identity.common.internal.dto.CredentialType;
import com.microsoft.identity.common.internal.dto.IdToken;
import com.microsoft.identity.common.internal.dto.RefreshToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.microsoft.identity.common.internal.cache.CacheKeyValueDelegate.CACHE_VALUE_SEPARATOR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CacheKeyValueDelegateTest {

    private static final String HOME_ACCOUNT_ID = "29f3807a-4fb0-42f2-a44a-236aa0cb3f97.0287f963-2d72-4363-9e3a-5705c5b0f031";
    private static final String ENVIRONMENT = "login.microsoftonline.com";
    private static final String CLIENT_ID = "0287f963-2d72-4363-9e3a-5705c5b0f031";
    private static final String TARGET = "user.read user.write https://graph.windows.net";
    private static final String REALM = "3c62ac97-29eb-4aed-a3c8-add0298508d";
    private static final String CREDENTIAL_TYPE_ACCESS_TOKEN = CredentialType.AccessToken.name().toLowerCase(Locale.US);
    private static final String CREDENTIAL_TYPE_REFRESH_TOKEN = CredentialType.RefreshToken.name().toLowerCase(Locale.US);
    private static final String CREDENTIAL_TYPE_ID_TOKEN = CredentialType.IdToken.name().toLowerCase(Locale.US);
    private static final String AUTHORITY_ACCOUNT_ID = "90bc88e6-7c76-45e8-a4e3-a0b1dc0a8ce1";
    private static final String AUTHORITY_TYPE = "AAD";
    private static final String GUEST_ID = "32000000000003bde810";
    private static final String FIRST_NAME = "Jane";
    private static final String LAST_NAME = "Doe";
    private static final String AVATAR_URL = "https://fake.cdn.microsoft.com/avatars/1";

    private ICacheKeyValueDelegate mDelegate;

    @Before
    public void setUp() {
        mDelegate = new CacheKeyValueDelegate();
    }

    // AccessTokens
    @Test
    public void accessTokenCreateCacheKeyComplete() throws Exception {
        final AccessToken accessToken = new AccessToken();
        accessToken.setHomeAccountId(HOME_ACCOUNT_ID);
        accessToken.setEnvironment(ENVIRONMENT);
        accessToken.setCredentialType(CredentialType.AccessToken.name());
        accessToken.setClientId(CLIENT_ID);
        accessToken.setRealm(REALM);
        accessToken.setTarget(TARGET);

        final String expectedKey = "" // just for formatting
                + HOME_ACCOUNT_ID + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + CREDENTIAL_TYPE_ACCESS_TOKEN + CACHE_VALUE_SEPARATOR
                + CLIENT_ID + CACHE_VALUE_SEPARATOR
                + REALM + CACHE_VALUE_SEPARATOR
                + TARGET;
        assertEquals(expectedKey, mDelegate.generateCacheKey(accessToken));
    }

    @Test
    public void accessTokenCreateCacheKeyNoUniqueId() throws Exception {
        final AccessToken accessToken = new AccessToken();
        accessToken.setEnvironment(ENVIRONMENT);
        accessToken.setCredentialType(CredentialType.AccessToken.name());
        accessToken.setClientId(CLIENT_ID);
        accessToken.setRealm(REALM);
        accessToken.setTarget(TARGET);

        final String expectedKey = "" // just for formatting
                + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + CREDENTIAL_TYPE_ACCESS_TOKEN + CACHE_VALUE_SEPARATOR
                + CLIENT_ID + CACHE_VALUE_SEPARATOR
                + REALM + CACHE_VALUE_SEPARATOR
                + TARGET;
        assertEquals(expectedKey, mDelegate.generateCacheKey(accessToken));
    }

    @Test
    public void accessTokenCreateCacheKeyNoRealm() throws Exception {
        final AccessToken accessToken = new AccessToken();
        accessToken.setHomeAccountId(HOME_ACCOUNT_ID);
        accessToken.setEnvironment(ENVIRONMENT);
        accessToken.setCredentialType(CredentialType.AccessToken.name());
        accessToken.setClientId(CLIENT_ID);
        accessToken.setTarget(TARGET);

        final String expectedKey = "" // just for formatting
                + HOME_ACCOUNT_ID + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + CREDENTIAL_TYPE_ACCESS_TOKEN + CACHE_VALUE_SEPARATOR
                + CLIENT_ID + CACHE_VALUE_SEPARATOR
                + CACHE_VALUE_SEPARATOR
                + TARGET;
        assertEquals(expectedKey, mDelegate.generateCacheKey(accessToken));
    }

    @Test
    public void accessTokenCreateCacheKeyNoTarget() throws Exception {
        final AccessToken accessToken = new AccessToken();
        accessToken.setHomeAccountId(HOME_ACCOUNT_ID);
        accessToken.setEnvironment(ENVIRONMENT);
        accessToken.setCredentialType(CredentialType.AccessToken.name());
        accessToken.setClientId(CLIENT_ID);
        accessToken.setRealm(REALM);

        final String expectedKey = "" // just for formatting
                + HOME_ACCOUNT_ID + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + CREDENTIAL_TYPE_ACCESS_TOKEN + CACHE_VALUE_SEPARATOR
                + CLIENT_ID + CACHE_VALUE_SEPARATOR
                + REALM + CACHE_VALUE_SEPARATOR;

        assertEquals(expectedKey, mDelegate.generateCacheKey(accessToken));
    }

    @Test
    public void accessTokenCreateCacheKeyNoUniqueIdNoRealmNoTarget() throws Exception {
        final AccessToken accessToken = new AccessToken();
        accessToken.setEnvironment(ENVIRONMENT);
        accessToken.setCredentialType(CredentialType.AccessToken.name());
        accessToken.setClientId(CLIENT_ID);

        final String expectedKey = "" // just for formatting
                + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + CREDENTIAL_TYPE_ACCESS_TOKEN + CACHE_VALUE_SEPARATOR
                + CLIENT_ID + CACHE_VALUE_SEPARATOR
                + CACHE_VALUE_SEPARATOR;

        assertEquals(expectedKey, mDelegate.generateCacheKey(accessToken));
    }

    @Test
    public void accessTokenCreateCacheKeyNoRealmNoTarget() throws Exception {
        final AccessToken accessToken = new AccessToken();
        accessToken.setHomeAccountId(HOME_ACCOUNT_ID);
        accessToken.setEnvironment(ENVIRONMENT);
        accessToken.setCredentialType(CredentialType.AccessToken.name());
        accessToken.setClientId(CLIENT_ID);

        final String expectedKey = "" // just for formatting
                + HOME_ACCOUNT_ID + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + CREDENTIAL_TYPE_ACCESS_TOKEN + CACHE_VALUE_SEPARATOR
                + CLIENT_ID + CACHE_VALUE_SEPARATOR
                + CACHE_VALUE_SEPARATOR;

        assertEquals(expectedKey, mDelegate.generateCacheKey(accessToken));
    }

    @Test
    public void accessTokenCreateCacheValue() throws Exception {
        final AccessToken accessToken = new AccessToken();
        accessToken.setHomeAccountId(HOME_ACCOUNT_ID);
        accessToken.setEnvironment(ENVIRONMENT);
        accessToken.setCredentialType(CredentialType.AccessToken.name().toLowerCase(Locale.US));
        accessToken.setClientId(CLIENT_ID);
        accessToken.setRealm(REALM);
        accessToken.setTarget(TARGET);

        final String serializedValue = mDelegate.generateCacheValue(accessToken);

        // Turn the serialized value into a JSONObject and start testing field equality.
        final JSONObject jsonObject = new JSONObject(serializedValue);
        assertEquals(HOME_ACCOUNT_ID, jsonObject.getString(Credential.SerializedNames.HOME_ACCOUNT_ID));
        assertEquals(ENVIRONMENT, jsonObject.getString(Credential.SerializedNames.ENVIRONMENT));
        assertEquals(CredentialType.AccessToken.name().toLowerCase(Locale.US), jsonObject.getString("credential_type"));
        assertEquals(CLIENT_ID, jsonObject.getString(Credential.SerializedNames.CLIENT_ID));
        assertEquals(REALM, jsonObject.getString(AccessToken.SerializedNames.REALM));
        assertEquals(TARGET, jsonObject.getString(AccessToken.SerializedNames.TARGET));
    }

    @Test
    public void accessTokenExtraValueSerialization() throws Exception {
        final AccessToken accessToken = new AccessToken();
        accessToken.setHomeAccountId(HOME_ACCOUNT_ID);
        accessToken.setEnvironment(ENVIRONMENT);
        accessToken.setCredentialType(CredentialType.AccessToken.name().toLowerCase(Locale.US));
        accessToken.setClientId(CLIENT_ID);
        accessToken.setRealm(REALM);
        accessToken.setTarget(TARGET);

        final Map<String, JsonElement> additionalFields = new HashMap<>();

        // Add some random Json to this object
        JsonElement jsonStr = new JsonPrimitive("bar");
        JsonArray jsonNumberArr = new JsonArray();
        jsonNumberArr.add(1);
        jsonNumberArr.add(2);
        jsonNumberArr.add(3);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("object_key", new JsonPrimitive("object_value"));

        additionalFields.put("foo", jsonStr);
        additionalFields.put("numbers", jsonNumberArr);
        additionalFields.put("object", jsonObject);

        accessToken.setAdditionalFields(additionalFields);

        String serializedValue = mDelegate.generateCacheValue(accessToken);
        JSONObject derivedCacheValueJsonObject = new JSONObject(serializedValue);
        assertEquals(HOME_ACCOUNT_ID, derivedCacheValueJsonObject.getString(Credential.SerializedNames.HOME_ACCOUNT_ID));
        assertEquals(ENVIRONMENT, derivedCacheValueJsonObject.getString(Credential.SerializedNames.ENVIRONMENT));
        assertEquals(CredentialType.AccessToken.name().toLowerCase(Locale.US), derivedCacheValueJsonObject.getString("credential_type"));
        assertEquals(CLIENT_ID, derivedCacheValueJsonObject.getString(Credential.SerializedNames.CLIENT_ID));
        assertEquals(REALM, derivedCacheValueJsonObject.getString(AccessToken.SerializedNames.REALM));
        assertEquals(TARGET, derivedCacheValueJsonObject.getString(AccessToken.SerializedNames.TARGET));
        assertEquals("bar", derivedCacheValueJsonObject.getString("foo"));

        final JSONArray jsonArr = derivedCacheValueJsonObject.getJSONArray("numbers");
        assertEquals(3, jsonArr.length());

        final JSONObject jsonObj = derivedCacheValueJsonObject.getJSONObject("object");
        assertEquals("object_value", jsonObj.getString("object_key"));
    }

    @Test
    public void accessTokenExtraValueDeserialization() throws Exception {
        final AccessToken accessToken = new AccessToken();
        accessToken.setHomeAccountId(HOME_ACCOUNT_ID);
        accessToken.setEnvironment(ENVIRONMENT);
        accessToken.setCredentialType(CredentialType.AccessToken.name().toLowerCase(Locale.US));
        accessToken.setClientId(CLIENT_ID);
        accessToken.setRealm(REALM);
        accessToken.setTarget(TARGET);

        String serializedValue = mDelegate.generateCacheValue(accessToken);

        // Turn the serialized value into a JSONObject and start testing field equality.
        final JSONObject jsonObject = new JSONObject(serializedValue);

        // Add more non-standard data to this object...
        final JSONArray numbers = new JSONArray("[1, 2, 3]");
        final JSONArray objects = new JSONArray("[{\"hello\" : \"hallo\"}, {\"goodbye\" : \"auf wiedersehen\"}]");

        jsonObject.put("foo", "bar");
        jsonObject.put("numbers", numbers);
        jsonObject.put("objects", objects);

        serializedValue = jsonObject.toString();

        final AccessToken deserializedValue = mDelegate.fromCacheValue(serializedValue, AccessToken.class);
        assertNotNull(deserializedValue);
        assertNull(deserializedValue.getAdditionalFields().get(Credential.SerializedNames.ENVIRONMENT));
        assertEquals(HOME_ACCOUNT_ID, deserializedValue.getHomeAccountId());
        assertEquals(ENVIRONMENT, deserializedValue.getEnvironment());
        assertEquals(CredentialType.AccessToken.name().toLowerCase(Locale.US), deserializedValue.getCredentialType());
        assertEquals(CLIENT_ID, deserializedValue.getClientId());
        assertEquals(REALM, deserializedValue.getRealm());
        assertEquals(TARGET, deserializedValue.getTarget());
        assertEquals(3, deserializedValue.getAdditionalFields().size());
        assertEquals("bar", deserializedValue.getAdditionalFields().get("foo").getAsString());
        assertEquals(numbers.toString(), deserializedValue.getAdditionalFields().get("numbers").toString());
    }
    // End AccessTokens

    // Accounts
    @Test
    public void accountCreateCacheKeyComplete() throws Exception {
        final com.microsoft.identity.common.internal.dto.Account account = new com.microsoft.identity.common.internal.dto.Account();
        account.setHomeAccountId(HOME_ACCOUNT_ID);
        account.setEnvironment(ENVIRONMENT);
        account.setRealm(REALM);

        final String expectedKey = ""
                + HOME_ACCOUNT_ID + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + REALM;

        assertEquals(expectedKey, mDelegate.generateCacheKey(account));
    }

    @Test
    public void accountCreateCacheKeyCompleteNoUniqueId() throws Exception {
        final com.microsoft.identity.common.internal.dto.Account account = new com.microsoft.identity.common.internal.dto.Account();
        account.setEnvironment(ENVIRONMENT);
        account.setRealm(REALM);

        final String expectedKey = ""
                + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + REALM;

        assertEquals(expectedKey, mDelegate.generateCacheKey(account));
    }

    @Test
    public void accountCreateCacheKeyCompleteNoRealm() throws Exception {
        final com.microsoft.identity.common.internal.dto.Account account = new com.microsoft.identity.common.internal.dto.Account();
        account.setHomeAccountId(HOME_ACCOUNT_ID);
        account.setEnvironment(ENVIRONMENT);

        final String expectedKey = ""
                + HOME_ACCOUNT_ID + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR;

        assertEquals(expectedKey, mDelegate.generateCacheKey(account));
    }

    @Test
    public void accountCreateCacheKeyCompleteNoUniqueIdNoRealm() throws Exception {
        final com.microsoft.identity.common.internal.dto.Account account = new com.microsoft.identity.common.internal.dto.Account();
        account.setEnvironment(ENVIRONMENT);

        final String expectedKey = ""
                + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR;

        assertEquals(expectedKey, mDelegate.generateCacheKey(account));
    }

    @Test
    public void accountCreateCacheValue() throws Exception {
        final com.microsoft.identity.common.internal.dto.Account account = new com.microsoft.identity.common.internal.dto.Account();
        account.setHomeAccountId(HOME_ACCOUNT_ID);
        account.setEnvironment(ENVIRONMENT);
        account.setRealm(REALM);
        account.setAuthorityAccountId(AUTHORITY_ACCOUNT_ID);
        account.setAuthorityType(AUTHORITY_TYPE);
        account.setGuestId(GUEST_ID);
        account.setFirstName(FIRST_NAME);
        account.setLastName(LAST_NAME);
        account.setAvatarUrl(AVATAR_URL);

        final String serializedValue = mDelegate.generateCacheValue(account);

        final JSONObject jsonObject = new JSONObject(serializedValue);
        assertEquals(HOME_ACCOUNT_ID, jsonObject.getString(com.microsoft.identity.common.internal.dto.Account.SerializedNames.HOME_ACCOUNT_ID));
        assertEquals(ENVIRONMENT, jsonObject.getString(com.microsoft.identity.common.internal.dto.Account.SerializedNames.ENVIRONMENT));
        assertEquals(REALM, jsonObject.getString(com.microsoft.identity.common.internal.dto.Account.SerializedNames.REALM));
        assertEquals(AUTHORITY_ACCOUNT_ID, jsonObject.getString("authority_account_id"));
        assertEquals(AUTHORITY_TYPE, jsonObject.getString("authority_type"));
        assertEquals(GUEST_ID, jsonObject.getString("guest_id"));
        assertEquals(FIRST_NAME, jsonObject.getString("first_name"));
        assertEquals(LAST_NAME, jsonObject.getString("last_name"));
        assertEquals(AVATAR_URL, jsonObject.getString("avatar_url"));
    }

    @Test
    public void accountExtraValueSerialization() throws Exception {
        final com.microsoft.identity.common.internal.dto.Account account
                = new com.microsoft.identity.common.internal.dto.Account();
        account.setHomeAccountId(HOME_ACCOUNT_ID);
        account.setEnvironment(ENVIRONMENT);
        account.setRealm(REALM);
        account.setAuthorityAccountId(AUTHORITY_ACCOUNT_ID);
        account.setAuthorityType(AUTHORITY_TYPE);
        account.setGuestId(GUEST_ID);
        account.setFirstName(FIRST_NAME);
        account.setLastName(LAST_NAME);
        account.setAvatarUrl(AVATAR_URL);

        final Map<String, JsonElement> additionalFields = new HashMap<>();

        // Add some random Json to this object
        JsonElement jsonStr = new JsonPrimitive("bar");
        JsonArray jsonNumberArr = new JsonArray();
        jsonNumberArr.add(1);
        jsonNumberArr.add(2);
        jsonNumberArr.add(3);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("object_key", new JsonPrimitive("object_value"));

        additionalFields.put("foo", jsonStr);
        additionalFields.put("numbers", jsonNumberArr);
        additionalFields.put("object", jsonObject);

        account.setAdditionalFields(additionalFields);

        String serializedValue = mDelegate.generateCacheValue(account);
        JSONObject derivedCacheValueJsonObject = new JSONObject(serializedValue);
        assertEquals(HOME_ACCOUNT_ID, derivedCacheValueJsonObject.getString(com.microsoft.identity.common.internal.dto.Account.SerializedNames.HOME_ACCOUNT_ID));
        assertEquals(ENVIRONMENT, derivedCacheValueJsonObject.getString(com.microsoft.identity.common.internal.dto.Account.SerializedNames.ENVIRONMENT));
        assertEquals(REALM, derivedCacheValueJsonObject.getString(com.microsoft.identity.common.internal.dto.Account.SerializedNames.REALM));
        assertEquals(AUTHORITY_ACCOUNT_ID, derivedCacheValueJsonObject.get("authority_account_id"));
        assertEquals(AUTHORITY_TYPE, derivedCacheValueJsonObject.get("authority_type"));
        assertEquals(GUEST_ID, derivedCacheValueJsonObject.get("guest_id"));
        assertEquals(FIRST_NAME, derivedCacheValueJsonObject.get("first_name"));
        assertEquals(LAST_NAME, derivedCacheValueJsonObject.get("last_name"));
        assertEquals(AVATAR_URL, derivedCacheValueJsonObject.get("avatar_url"));
        assertEquals("bar", derivedCacheValueJsonObject.getString("foo"));

        final JSONArray jsonArr = derivedCacheValueJsonObject.getJSONArray("numbers");
        assertEquals(3, jsonArr.length());

        final JSONObject jsonObj = derivedCacheValueJsonObject.getJSONObject("object");
        assertEquals("object_value", jsonObj.getString("object_key"));
    }

    @Test
    public void accountExtraValueDeserialization() throws Exception {
        final com.microsoft.identity.common.internal.dto.Account account
                = new com.microsoft.identity.common.internal.dto.Account();
        account.setHomeAccountId(HOME_ACCOUNT_ID);
        account.setEnvironment(ENVIRONMENT);
        account.setRealm(REALM);
        account.setAuthorityAccountId(AUTHORITY_ACCOUNT_ID);
        account.setAuthorityType(AUTHORITY_TYPE);
        account.setGuestId(GUEST_ID);
        account.setFirstName(FIRST_NAME);
        account.setLastName(LAST_NAME);
        account.setAvatarUrl(AVATAR_URL);

        String serializedValue = mDelegate.generateCacheValue(account);

        // Turn the serialized value into a JSONObject and start testing field equality.
        final JSONObject jsonObject = new JSONObject(serializedValue);

        // Add more non-standard data to this object...
        final JSONArray numbers = new JSONArray("[1, 2, 3]");
        final JSONArray objects = new JSONArray("[{\"hello\" : \"hallo\"}, {\"goodbye\" : \"auf wiedersehen\"}]");

        jsonObject.put("foo", "bar");
        jsonObject.put("numbers", numbers);
        jsonObject.put("objects", objects);

        serializedValue = jsonObject.toString();

        final com.microsoft.identity.common.internal.dto.Account deserializedValue
                = mDelegate.fromCacheValue(serializedValue, com.microsoft.identity.common.internal.dto.Account.class);
        assertNotNull(deserializedValue);
        assertNull(deserializedValue.getAdditionalFields().get(com.microsoft.identity.common.internal.dto.Account.SerializedNames.ENVIRONMENT));
        assertEquals(HOME_ACCOUNT_ID, deserializedValue.getHomeAccountId());
        assertEquals(ENVIRONMENT, deserializedValue.getEnvironment());
        assertEquals(REALM, deserializedValue.getRealm());
        assertEquals(AUTHORITY_ACCOUNT_ID, deserializedValue.getAuthorityAccountId());
        assertEquals(AUTHORITY_TYPE, deserializedValue.getAuthorityType());
        assertEquals(GUEST_ID, deserializedValue.getGuestId());
        assertEquals(FIRST_NAME, deserializedValue.getFirstName());
        assertEquals(LAST_NAME, deserializedValue.getLastName());
        assertEquals(AVATAR_URL, deserializedValue.getAvatarUrl());
        assertEquals(3, deserializedValue.getAdditionalFields().size());
        assertEquals("bar", deserializedValue.getAdditionalFields().get("foo").getAsString());
        assertEquals(numbers.toString(), deserializedValue.getAdditionalFields().get("numbers").toString());
    }
    // End Accounts

    // RefreshTokens
    @Test
    public void refreshTokenCreateCacheKeyComplete() throws Exception {
        final RefreshToken refreshToken = new RefreshToken();
        refreshToken.setHomeAccountId(HOME_ACCOUNT_ID);
        refreshToken.setEnvironment(ENVIRONMENT);
        refreshToken.setCredentialType(CredentialType.RefreshToken.name());
        refreshToken.setClientId(CLIENT_ID);
        refreshToken.setTarget(TARGET);

        final String expectedKey = "" // just for formatting
                + HOME_ACCOUNT_ID + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + CREDENTIAL_TYPE_REFRESH_TOKEN + CACHE_VALUE_SEPARATOR
                + CLIENT_ID + CACHE_VALUE_SEPARATOR
                + CACHE_VALUE_SEPARATOR
                + TARGET;
        assertEquals(expectedKey, mDelegate.generateCacheKey(refreshToken));
    }

    @Test
    public void refreshTokenCreateCacheKeyNoUniqueId() throws Exception {
        final RefreshToken refreshToken = new RefreshToken();
        refreshToken.setEnvironment(ENVIRONMENT);
        refreshToken.setCredentialType(CredentialType.RefreshToken.name());
        refreshToken.setClientId(CLIENT_ID);
        refreshToken.setTarget(TARGET);

        final String expectedKey = "" // just for formatting
                + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + CREDENTIAL_TYPE_REFRESH_TOKEN + CACHE_VALUE_SEPARATOR
                + CLIENT_ID + CACHE_VALUE_SEPARATOR
                + CACHE_VALUE_SEPARATOR
                + TARGET;
        assertEquals(expectedKey, mDelegate.generateCacheKey(refreshToken));
    }

    @Test
    public void refreshTokenCreateCacheKeyNoRealm() throws Exception {
        final RefreshToken refreshToken = new RefreshToken();
        refreshToken.setHomeAccountId(HOME_ACCOUNT_ID);
        refreshToken.setEnvironment(ENVIRONMENT);
        refreshToken.setCredentialType(CredentialType.RefreshToken.name());
        refreshToken.setClientId(CLIENT_ID);
        refreshToken.setTarget(TARGET);

        final String expectedKey = "" // just for formatting
                + HOME_ACCOUNT_ID + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + CREDENTIAL_TYPE_REFRESH_TOKEN + CACHE_VALUE_SEPARATOR
                + CLIENT_ID + CACHE_VALUE_SEPARATOR
                + CACHE_VALUE_SEPARATOR
                + TARGET;
        assertEquals(expectedKey, mDelegate.generateCacheKey(refreshToken));
    }

    @Test
    public void refreshTokenCreateCacheKeyNoTarget() throws Exception {
        final RefreshToken refreshToken = new RefreshToken();
        refreshToken.setHomeAccountId(HOME_ACCOUNT_ID);
        refreshToken.setEnvironment(ENVIRONMENT);
        refreshToken.setCredentialType(CredentialType.RefreshToken.name());
        refreshToken.setClientId(CLIENT_ID);

        final String expectedKey = "" // just for formatting
                + HOME_ACCOUNT_ID + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + CREDENTIAL_TYPE_REFRESH_TOKEN + CACHE_VALUE_SEPARATOR
                + CLIENT_ID + CACHE_VALUE_SEPARATOR
                + CACHE_VALUE_SEPARATOR;

        assertEquals(expectedKey, mDelegate.generateCacheKey(refreshToken));
    }

    @Test
    public void refreshTokenCreateCacheKeyNoUniqueIdNoTarget() throws Exception {
        final RefreshToken refreshToken = new RefreshToken();
        refreshToken.setEnvironment(ENVIRONMENT);
        refreshToken.setCredentialType(CredentialType.RefreshToken.name());
        refreshToken.setClientId(CLIENT_ID);

        final String expectedKey = "" // just for formatting
                + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + CREDENTIAL_TYPE_REFRESH_TOKEN + CACHE_VALUE_SEPARATOR
                + CLIENT_ID + CACHE_VALUE_SEPARATOR
                + CACHE_VALUE_SEPARATOR;

        assertEquals(expectedKey, mDelegate.generateCacheKey(refreshToken));
    }

    @Test
    public void refreshTokenCreateCacheValue() throws Exception {
        final RefreshToken refreshToken = new RefreshToken();
        refreshToken.setHomeAccountId(HOME_ACCOUNT_ID);
        refreshToken.setEnvironment(ENVIRONMENT);
        refreshToken.setCredentialType(CredentialType.RefreshToken.name().toLowerCase(Locale.US));
        refreshToken.setClientId(CLIENT_ID);
        refreshToken.setTarget(TARGET);

        final String serializedValue = mDelegate.generateCacheValue(refreshToken);

        // Turn the serialized value into a JSONObject and start testing field equality.
        final JSONObject jsonObject = new JSONObject(serializedValue);
        assertEquals(HOME_ACCOUNT_ID, jsonObject.getString(RefreshToken.SerializedNames.HOME_ACCOUNT_ID));
        assertEquals(ENVIRONMENT, jsonObject.getString(RefreshToken.SerializedNames.ENVIRONMENT));
        assertEquals(CredentialType.RefreshToken.name().toLowerCase(Locale.US), jsonObject.getString("credential_type"));
        assertEquals(CLIENT_ID, jsonObject.getString(RefreshToken.SerializedNames.CLIENT_ID));
        assertEquals(TARGET, jsonObject.getString(RefreshToken.SerializedNames.TARGET));
    }

    @Test
    public void refreshTokenExtraValueSerialization() throws Exception {
        final RefreshToken refreshToken = new RefreshToken();
        refreshToken.setHomeAccountId(HOME_ACCOUNT_ID);
        refreshToken.setEnvironment(ENVIRONMENT);
        refreshToken.setCredentialType(CredentialType.RefreshToken.name().toLowerCase(Locale.US));
        refreshToken.setClientId(CLIENT_ID);
        refreshToken.setTarget(TARGET);

        final Map<String, JsonElement> additionalFields = new HashMap<>();

        // Add some random Json to this object
        JsonElement jsonStr = new JsonPrimitive("bar");
        JsonArray jsonNumberArr = new JsonArray();
        jsonNumberArr.add(1);
        jsonNumberArr.add(2);
        jsonNumberArr.add(3);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("object_key", new JsonPrimitive("object_value"));

        additionalFields.put("foo", jsonStr);
        additionalFields.put("numbers", jsonNumberArr);
        additionalFields.put("object", jsonObject);

        refreshToken.setAdditionalFields(additionalFields);

        String serializedValue = mDelegate.generateCacheValue(refreshToken);
        JSONObject derivedCacheValueJsonObject = new JSONObject(serializedValue);
        assertEquals(HOME_ACCOUNT_ID, derivedCacheValueJsonObject.getString(RefreshToken.SerializedNames.HOME_ACCOUNT_ID));
        assertEquals(ENVIRONMENT, derivedCacheValueJsonObject.getString(RefreshToken.SerializedNames.ENVIRONMENT));
        assertEquals(CredentialType.RefreshToken.name().toLowerCase(Locale.US), derivedCacheValueJsonObject.getString("credential_type"));
        assertEquals(CLIENT_ID, derivedCacheValueJsonObject.getString(RefreshToken.SerializedNames.CLIENT_ID));
        assertEquals(TARGET, derivedCacheValueJsonObject.getString(RefreshToken.SerializedNames.TARGET));
        assertEquals("bar", derivedCacheValueJsonObject.getString("foo"));

        final JSONArray jsonArr = derivedCacheValueJsonObject.getJSONArray("numbers");
        assertEquals(3, jsonArr.length());

        final JSONObject jsonObj = derivedCacheValueJsonObject.getJSONObject("object");
        assertEquals("object_value", jsonObj.getString("object_key"));
    }

    @Test
    public void refreshTokenExtraValueDeserialization() throws Exception {
        final RefreshToken refreshToken = new RefreshToken();
        refreshToken.setHomeAccountId(HOME_ACCOUNT_ID);
        refreshToken.setEnvironment(ENVIRONMENT);
        refreshToken.setCredentialType(CredentialType.RefreshToken.name().toLowerCase(Locale.US));
        refreshToken.setClientId(CLIENT_ID);
        refreshToken.setTarget(TARGET);

        String serializedValue = mDelegate.generateCacheValue(refreshToken);

        // Turn the serialized value into a JSONObject and start testing field equality.
        final JSONObject jsonObject = new JSONObject(serializedValue);

        // Add more non-standard data to this object...
        final JSONArray numbers = new JSONArray("[1, 2, 3]");
        final JSONArray objects = new JSONArray("[{\"hello\" : \"hallo\"}, {\"goodbye\" : \"auf wiedersehen\"}]");

        jsonObject.put("foo", "bar");
        jsonObject.put("numbers", numbers);
        jsonObject.put("objects", objects);

        serializedValue = jsonObject.toString();

        final RefreshToken deserializedValue = mDelegate.fromCacheValue(serializedValue, RefreshToken.class);
        assertNotNull(deserializedValue);
        assertNull(deserializedValue.getAdditionalFields().get(Credential.SerializedNames.ENVIRONMENT));
        assertEquals(HOME_ACCOUNT_ID, deserializedValue.getHomeAccountId());
        assertEquals(ENVIRONMENT, deserializedValue.getEnvironment());
        assertEquals(CredentialType.RefreshToken.name().toLowerCase(Locale.US), deserializedValue.getCredentialType());
        assertEquals(CLIENT_ID, deserializedValue.getClientId());
        assertEquals(TARGET, deserializedValue.getTarget());
        assertEquals(3, deserializedValue.getAdditionalFields().size());
        assertEquals("bar", deserializedValue.getAdditionalFields().get("foo").getAsString());
        assertEquals(numbers.toString(), deserializedValue.getAdditionalFields().get("numbers").toString());
    }
    // End RefreshTokens

    // IdTokens
    @Test
    public void idTokenCreateCacheKeyComplete() throws Exception {
        final IdToken idToken = new IdToken();
        idToken.setHomeAccountId(HOME_ACCOUNT_ID);
        idToken.setEnvironment(ENVIRONMENT);
        idToken.setCredentialType(CredentialType.IdToken.name());
        idToken.setClientId(CLIENT_ID);
        idToken.setRealm(REALM);

        final String expectedKey = "" // just for formatting
                + HOME_ACCOUNT_ID + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + CREDENTIAL_TYPE_ID_TOKEN + CACHE_VALUE_SEPARATOR
                + CLIENT_ID + CACHE_VALUE_SEPARATOR
                + REALM + CACHE_VALUE_SEPARATOR;
        assertEquals(expectedKey, mDelegate.generateCacheKey(idToken));
    }

    @Test
    public void idTokenCreateCacheKeyNoUniqueId() throws Exception {
        final IdToken idToken = new IdToken();
        idToken.setEnvironment(ENVIRONMENT);
        idToken.setCredentialType(CredentialType.IdToken.name());
        idToken.setClientId(CLIENT_ID);
        idToken.setRealm(REALM);

        final String expectedKey = "" // just for formatting
                + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + CREDENTIAL_TYPE_ID_TOKEN + CACHE_VALUE_SEPARATOR
                + CLIENT_ID + CACHE_VALUE_SEPARATOR
                + REALM + CACHE_VALUE_SEPARATOR;
        assertEquals(expectedKey, mDelegate.generateCacheKey(idToken));
    }

    @Test
    public void idTokenCreateCacheKeyNoRealm() throws Exception {
        final IdToken idToken = new IdToken();
        idToken.setHomeAccountId(HOME_ACCOUNT_ID);
        idToken.setEnvironment(ENVIRONMENT);
        idToken.setCredentialType(CredentialType.IdToken.name());
        idToken.setClientId(CLIENT_ID);

        final String expectedKey = "" // just for formatting
                + HOME_ACCOUNT_ID + CACHE_VALUE_SEPARATOR
                + ENVIRONMENT + CACHE_VALUE_SEPARATOR
                + CREDENTIAL_TYPE_ID_TOKEN + CACHE_VALUE_SEPARATOR
                + CLIENT_ID + CACHE_VALUE_SEPARATOR
                + CACHE_VALUE_SEPARATOR;
        assertEquals(expectedKey, mDelegate.generateCacheKey(idToken));
    }

    @Test
    public void idTokenCreateCacheValue() throws Exception {
        final IdToken idToken = new IdToken();
        idToken.setHomeAccountId(HOME_ACCOUNT_ID);
        idToken.setEnvironment(ENVIRONMENT);
        idToken.setCredentialType(CredentialType.IdToken.name().toLowerCase(Locale.US));
        idToken.setClientId(CLIENT_ID);
        idToken.setRealm(REALM);

        final String serializedValue = mDelegate.generateCacheValue(idToken);

        // Turn the serialized value into a JSONObject and start testing field equality.
        final JSONObject jsonObject = new JSONObject(serializedValue);
        assertEquals(HOME_ACCOUNT_ID, jsonObject.getString(Credential.SerializedNames.HOME_ACCOUNT_ID));
        assertEquals(ENVIRONMENT, jsonObject.getString(Credential.SerializedNames.ENVIRONMENT));
        assertEquals(CredentialType.IdToken.name().toLowerCase(Locale.US), jsonObject.getString("credential_type"));
        assertEquals(CLIENT_ID, jsonObject.getString(Credential.SerializedNames.CLIENT_ID));
        assertEquals(REALM, jsonObject.getString(IdToken.SerializedNames.REALM));
    }

    @Test
    public void idTokenExtraValueSerialization() throws Exception {
        final IdToken idToken = new IdToken();
        idToken.setHomeAccountId(HOME_ACCOUNT_ID);
        idToken.setEnvironment(ENVIRONMENT);
        idToken.setCredentialType(CredentialType.IdToken.name().toLowerCase(Locale.US));
        idToken.setClientId(CLIENT_ID);
        idToken.setRealm(REALM);

        final Map<String, JsonElement> additionalFields = new HashMap<>();

        // Add some random Json to this object
        JsonElement jsonStr = new JsonPrimitive("bar");
        JsonArray jsonNumberArr = new JsonArray();
        jsonNumberArr.add(1);
        jsonNumberArr.add(2);
        jsonNumberArr.add(3);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("object_key", new JsonPrimitive("object_value"));

        additionalFields.put("foo", jsonStr);
        additionalFields.put("numbers", jsonNumberArr);
        additionalFields.put("object", jsonObject);

        idToken.setAdditionalFields(additionalFields);

        String serializedValue = mDelegate.generateCacheValue(idToken);
        JSONObject derivedCacheValueJsonObject = new JSONObject(serializedValue);
        assertEquals(HOME_ACCOUNT_ID, derivedCacheValueJsonObject.getString(Credential.SerializedNames.HOME_ACCOUNT_ID));
        assertEquals(ENVIRONMENT, derivedCacheValueJsonObject.getString(Credential.SerializedNames.ENVIRONMENT));
        assertEquals(CredentialType.IdToken.name().toLowerCase(Locale.US), derivedCacheValueJsonObject.getString("credential_type"));
        assertEquals(CLIENT_ID, derivedCacheValueJsonObject.getString(Credential.SerializedNames.CLIENT_ID));
        assertEquals(REALM, derivedCacheValueJsonObject.getString(IdToken.SerializedNames.REALM));
        assertEquals("bar", derivedCacheValueJsonObject.getString("foo"));

        final JSONArray jsonArr = derivedCacheValueJsonObject.getJSONArray("numbers");
        assertEquals(3, jsonArr.length());

        final JSONObject jsonObj = derivedCacheValueJsonObject.getJSONObject("object");
        assertEquals("object_value", jsonObj.getString("object_key"));
    }

    @Test
    public void idTokenExtraValueDeserialization() throws Exception {
        final IdToken idToken = new IdToken();
        idToken.setHomeAccountId(HOME_ACCOUNT_ID);
        idToken.setEnvironment(ENVIRONMENT);
        idToken.setCredentialType(CredentialType.IdToken.name().toLowerCase(Locale.US));
        idToken.setClientId(CLIENT_ID);
        idToken.setRealm(REALM);

        String serializedValue = mDelegate.generateCacheValue(idToken);

        // Turn the serialized value into a JSONObject and start testing field equality.
        final JSONObject jsonObject = new JSONObject(serializedValue);

        // Add more non-standard data to this object...
        final JSONArray numbers = new JSONArray("[1, 2, 3]");
        final JSONArray objects = new JSONArray("[{\"hello\" : \"hallo\"}, {\"goodbye\" : \"auf wiedersehen\"}]");

        jsonObject.put("foo", "bar");
        jsonObject.put("numbers", numbers);
        jsonObject.put("objects", objects);

        serializedValue = jsonObject.toString();

        final IdToken deserializedValue = mDelegate.fromCacheValue(serializedValue, IdToken.class);
        assertNotNull(deserializedValue);
        assertNull(deserializedValue.getAdditionalFields().get(Credential.SerializedNames.ENVIRONMENT));
        assertEquals(HOME_ACCOUNT_ID, deserializedValue.getHomeAccountId());
        assertEquals(ENVIRONMENT, deserializedValue.getEnvironment());
        assertEquals(CredentialType.IdToken.name().toLowerCase(Locale.US), deserializedValue.getCredentialType());
        assertEquals(CLIENT_ID, deserializedValue.getClientId());
        assertEquals(REALM, deserializedValue.getRealm());
        assertEquals(3, deserializedValue.getAdditionalFields().size());
        assertEquals("bar", deserializedValue.getAdditionalFields().get("foo").getAsString());
        assertEquals(numbers.toString(), deserializedValue.getAdditionalFields().get("numbers").toString());
    }
    // End IdTokens
}

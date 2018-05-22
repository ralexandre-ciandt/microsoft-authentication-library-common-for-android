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
package com.microsoft.identity.common.internal.providers.oauth2;

import com.google.gson.annotations.SerializedName;

/**
 * A class holding the state of the Token Request (oAuth2).
 * OAuth2 Spec: https://tools.ietf.org/html/rfc6749#section-4.1.3
 * OAuth2 Client Authentication: https://tools.ietf.org/html/rfc7521#section-4.2
 * This should include all fo the required parameters of the token request for oAuth2
 * This should provide an extension point for additional parameters to be set
 * <p>
 * Includes support for client assertions per the specs:
 * https://tools.ietf.org/html/rfc7521
 * https://docs.microsoft.com/en-us/azure/active-directory/develop/active-directory-v2-protocols-oauth-client-creds
 */
public class TokenRequest {

    @SerializedName("grant_type")
    private String mGrantType;

    @SerializedName("code")
    private String mCode;

    @SerializedName("redirect_uri")
    private String mRedirectUri;

    @SerializedName("client_id")
    private String mClientId;

    @SerializedName("client_secret")
    private String mClientSecret;

    @SerializedName("client_assertion_type")
    private String mClientAssertionType;

    @SerializedName("client_assertion")
    private String mClientAssertion;

    @SerializedName("scope")
    private String mScope;

    /**
     * @return mCode of the token request.
     */
    public String getCode() {
        return mCode;
    }

    /**
     * @param code code of the token request.
     */
    public void setCode(final String code) {
        mCode = code;
    }

    /**
     * @return mRedirectUri of the token request.
     */
    public String getRedirectUri() {
        return mRedirectUri;
    }

    /**
     * @param redirectUri redirect URI of the token request.
     */
    public void setRedirectUri(final String redirectUri) {
        mRedirectUri = redirectUri;
    }

    /**
     * @return mClientId of the token request.
     */
    public String getClientId() {
        return mClientId;
    }

    /**
     * @param clientId Client ID of the token request.
     */
    public void setClientId(final String clientId) {
        mClientId = clientId;
    }

    /**
     * @return mGrantType string of the token request.
     */
    public String getGrantType() {
        return mGrantType;
    }

    /**
     * @param grantType grant type string of the token request.
     */
    public void setGrantType(final String grantType) {
        mGrantType = grantType;
    }

    /**
     * @param clientSecret client secret string of the token request.
     */
    public void setClientSecret(final String clientSecret) {
        mClientSecret = clientSecret;
    }

    /**
     * @return mClientSecret of the token request.
     */
    public String getClientSecret() {
        return mClientSecret;
    }

    /**
     * @return mClientAssertionType of the token request.
     */
    public String getClientAssertionType() {
        return mClientAssertionType;
    }

    /**
     * @param clientAssertionType client assertion type of the token request.
     */
    public void setClientAssertionType(final String clientAssertionType) {
        mClientAssertionType = clientAssertionType;
    }

    /**
     * @return mClientAssertion of the token request.
     */
    public String getClientAssertion() {
        return mClientAssertion;
    }

    /**
     * @param clientAssertion client assertion of the token request.
     */
    public void setClientAssertion(final String clientAssertion) {
        mClientAssertion = clientAssertion;
    }

    /**
     * @return String mScope of the token request.
     */
    public String getScope() {
        return mScope;
    }

    /**
     * @param scope scope parameter of the token request.
     */
    public void setScope(final String scope) {
        mScope = scope;
    }
}
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
package com.microsoft.identity.common.internal.cache;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.microsoft.identity.common.exception.ServiceException;
import com.microsoft.identity.common.internal.dto.AccessTokenRecord;
import com.microsoft.identity.common.internal.dto.AccountRecord;
import com.microsoft.identity.common.internal.dto.CredentialType;
import com.microsoft.identity.common.internal.dto.IdTokenRecord;
import com.microsoft.identity.common.internal.dto.RefreshTokenRecord;
import com.microsoft.identity.common.internal.logging.Logger;
import com.microsoft.identity.common.internal.providers.microsoft.MicrosoftAccount;
import com.microsoft.identity.common.internal.providers.microsoft.MicrosoftRefreshToken;
import com.microsoft.identity.common.internal.providers.microsoft.azureactivedirectory.ClientInfo;
import com.microsoft.identity.common.internal.providers.microsoft.microsoftsts.MicrosoftStsAccount;
import com.microsoft.identity.common.internal.providers.microsoft.microsoftsts.MicrosoftStsAuthorizationRequest;
import com.microsoft.identity.common.internal.providers.microsoft.microsoftsts.MicrosoftStsOAuth2Strategy;
import com.microsoft.identity.common.internal.providers.microsoft.microsoftsts.MicrosoftStsTokenResponse;
import com.microsoft.identity.common.internal.providers.oauth2.IDToken;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.microsoft.identity.common.internal.authscheme.PopAuthenticationSchemeInternal.SCHEME_POP;
import static com.microsoft.identity.common.internal.controllers.BaseController.DEFAULT_SCOPES;

public class MicrosoftStsAccountCredentialAdapter
        implements IAccountCredentialAdapter
        <MicrosoftStsOAuth2Strategy,
                MicrosoftStsAuthorizationRequest,
                MicrosoftStsTokenResponse,
                MicrosoftAccount,
                MicrosoftRefreshToken> {

    private static final String TAG = MicrosoftStsAccountCredentialAdapter.class.getSimpleName();

    @Override
    public AccountRecord createAccount(
            final MicrosoftStsOAuth2Strategy strategy,
            final MicrosoftStsAuthorizationRequest request,
            final MicrosoftStsTokenResponse response) {
        Logger.verbose(TAG, "Creating Account");
        return new AccountRecord(strategy.createAccount(response));
    }

    @Override
    public AccessTokenRecord createAccessToken(
            final MicrosoftStsOAuth2Strategy strategy,
            final MicrosoftStsAuthorizationRequest request,
            final MicrosoftStsTokenResponse response) {
        try {
            final long cachedAt = getCachedAt();
            final long expiresOn = getExpiresOn(response);
            final ClientInfo clientInfo = new ClientInfo(response.getClientInfo());

            final AccessTokenRecord accessToken = new AccessTokenRecord();
            // Required fields
            accessToken.setCredentialType(getCredentialType(response.getTokenType()));
            accessToken.setHomeAccountId(SchemaUtil.getHomeAccountId(clientInfo));
            accessToken.setRealm(getRealm(strategy, response));
            accessToken.setEnvironment(strategy.getIssuerCacheIdentifierFromTokenEndpoint());
            accessToken.setClientId(request.getClientId());
            accessToken.setTarget(
                    getTarget(
                            request.getScope(),
                            response.getScope()
                    )
            );
            accessToken.setCachedAt(String.valueOf(cachedAt)); // generated @ client side
            accessToken.setExpiresOn(String.valueOf(expiresOn));
            accessToken.setSecret(response.getAccessToken());

            // Optional fields
            accessToken.setExtendedExpiresOn(getExtendedExpiresOn(response));
            accessToken.setAuthority(strategy.getAuthorityFromTokenEndpoint());
            accessToken.setAccessTokenType(response.getTokenType());

            // Use case insensitive match - ESTS will not capitalize scheme...
            if (SCHEME_POP.equalsIgnoreCase(response.getTokenType())) {
                accessToken.setKid(strategy.getDeviceAtPopThumbprint());
            }

            return accessToken;
        } catch (ServiceException e) {
            // TODO handle this properly
            throw new RuntimeException(e);
        }
    }

    private String getCredentialType(@NonNull final String tokenType) {
        // Assume default behavior; that token is of 'Bearer' auth scheme.
        String type = CredentialType.AccessToken.name();

        if (SCHEME_POP.equalsIgnoreCase(tokenType)) {
            return CredentialType.AccessToken_With_AuthScheme.name();
        }

        return type;
    }

    /**
     * Returns the correct target based on whether the default scopes were returned or not
     *
     * @param responseScope The response scope to parse.
     * @return The target containing default scopes.
     */
    private String getTarget(@NonNull final String requestScope,
                             @NonNull final String responseScope) {
        String scopesToCache = "";

        if (TextUtils.isEmpty(responseScope)) {
            // The response scopes were empty -- per https://tools.ietf.org/html/rfc6749#section-3.3
            // we are going to fall back to a the request scopes minus any default scopes....
            final String[] requestScopes = requestScope.split("\\s+");
            final Set<String> requestScopeSet = new HashSet<>(Arrays.asList(requestScopes));
            requestScopeSet.removeAll(DEFAULT_SCOPES);

            for (final String scope : requestScopeSet) {
                scopesToCache += scope + " ";
            }

            scopesToCache = scopesToCache.trim();
        } else {
            scopesToCache = responseScope;
        }

        return scopesToCache;
    }

    @Override
    public RefreshTokenRecord createRefreshToken(
            final MicrosoftStsOAuth2Strategy strategy,
            final MicrosoftStsAuthorizationRequest request,
            final MicrosoftStsTokenResponse response) {
        try {
            final long cachedAt = getCachedAt();
            final ClientInfo clientInfo = new ClientInfo(response.getClientInfo());

            final RefreshTokenRecord refreshToken = new RefreshTokenRecord();
            // Required
            refreshToken.setCredentialType(CredentialType.RefreshToken.name());
            refreshToken.setEnvironment(strategy.getIssuerCacheIdentifierFromTokenEndpoint());
            refreshToken.setHomeAccountId(SchemaUtil.getHomeAccountId(clientInfo));
            refreshToken.setClientId(request.getClientId());
            refreshToken.setSecret(response.getRefreshToken());

            // Optional
            refreshToken.setFamilyId(response.getFamilyId());
            refreshToken.setTarget(
                    getTarget(
                            request.getScope(),
                            response.getScope()
                    )
            );

            // TODO are these needed? Expected?
            refreshToken.setCachedAt(String.valueOf(cachedAt)); // generated @ client side

            return refreshToken;
        } catch (ServiceException e) {
            // TODO handle this properly
            throw new RuntimeException(e);
        }
    }

    @Override
    public IdTokenRecord createIdToken(
            final MicrosoftStsOAuth2Strategy strategy,
            final MicrosoftStsAuthorizationRequest request,
            final MicrosoftStsTokenResponse response) {
        try {
            final ClientInfo clientInfo = new ClientInfo(response.getClientInfo());

            final IdTokenRecord idToken = new IdTokenRecord();
            // Required fields
            idToken.setHomeAccountId(SchemaUtil.getHomeAccountId(clientInfo));
            idToken.setEnvironment(strategy.getIssuerCacheIdentifierFromTokenEndpoint());
            idToken.setRealm(getRealm(strategy, response));
            idToken.setCredentialType(
                    SchemaUtil.getCredentialTypeFromVersion(
                            response.getIdToken()
                    )
            );
            idToken.setClientId(request.getClientId());
            idToken.setSecret(response.getIdToken());
            idToken.setAuthority(strategy.getAuthorityFromTokenEndpoint());

            return idToken;
        } catch (ServiceException e) {
            // TODO handle this properly
            throw new RuntimeException(e);
        }
    }

    @Override
    public RefreshTokenRecord asRefreshToken(@NonNull final MicrosoftRefreshToken refreshTokenIn) {
        final RefreshTokenRecord refreshTokenOut = new RefreshTokenRecord();

        // Required fields
        refreshTokenOut.setHomeAccountId(refreshTokenIn.getHomeAccountId());
        refreshTokenOut.setEnvironment(refreshTokenIn.getEnvironment());
        refreshTokenOut.setCredentialType(CredentialType.RefreshToken.name());
        refreshTokenOut.setClientId(refreshTokenIn.getClientId());
        refreshTokenOut.setSecret(refreshTokenIn.getSecret());

        // Optional fields
        refreshTokenOut.setTarget(refreshTokenIn.getTarget());
        refreshTokenOut.setCachedAt(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
        refreshTokenOut.setFamilyId(refreshTokenIn.getFamilyId());

        return refreshTokenOut;
    }

    @Override
    public AccountRecord asAccount(MicrosoftAccount account) {
        AccountRecord acct = new AccountRecord(account);

        return acct;
    }

    @Override
    public IdTokenRecord asIdToken(MicrosoftAccount msAccount, MicrosoftRefreshToken refreshToken) {
        final long cachedAt = getCachedAt();
        IDToken msIdToken = msAccount.getIDToken();

        final IdTokenRecord idToken = new IdTokenRecord();
        // Required fields
        idToken.setHomeAccountId(refreshToken.getHomeAccountId());
        idToken.setEnvironment(refreshToken.getEnvironment());
        idToken.setRealm(msAccount.getRealm());
        idToken.setCredentialType(CredentialType.IdToken.name());
        idToken.setClientId(refreshToken.getClientId());
        idToken.setSecret(msIdToken.getRawIDToken());
        idToken.setCachedAt(String.valueOf(cachedAt));

        // Optional fields
        idToken.setAuthority(SchemaUtil.getAuthority(msIdToken));

        return idToken;
    }

    private String getExtendedExpiresOn(final MicrosoftStsTokenResponse response) {
        final long currentTimeMillis = System.currentTimeMillis();
        final long currentTimeSecs = TimeUnit.MILLISECONDS.toSeconds(currentTimeMillis);
        final long extExpiresIn = null == response.getExtExpiresIn() ? 0 : response.getExtExpiresIn();

        return String.valueOf(currentTimeSecs + extExpiresIn);
    }

    private String getRealm(final MicrosoftStsOAuth2Strategy msStrategy, final MicrosoftStsTokenResponse msTokenResponse) {
        final MicrosoftStsAccount msAccount = msStrategy.createAccount(msTokenResponse);
        return msAccount.getRealm();
    }

    private long getCachedAt() {
        final long currentTimeMillis = System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toSeconds(currentTimeMillis);
    }

    private long getExpiresOn(final MicrosoftStsTokenResponse msTokenResponse) {
        final long currentTimeMillis = System.currentTimeMillis();
        final long currentTimeSecs = TimeUnit.MILLISECONDS.toSeconds(currentTimeMillis);
        final long expiresIn = msTokenResponse.getExpiresIn();

        return currentTimeSecs + expiresIn;
    }

}

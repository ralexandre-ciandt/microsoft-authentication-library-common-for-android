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
package com.microsoft.identity.common.internal.dto;

import com.google.gson.annotations.SerializedName;

import static com.microsoft.identity.common.internal.dto.RefreshToken.SerializedNames.CLIENT_INFO;
import static com.microsoft.identity.common.internal.dto.RefreshToken.SerializedNames.FAMILY_ID;
import static com.microsoft.identity.common.internal.dto.RefreshToken.SerializedNames.TARGET;
import static com.microsoft.identity.common.internal.dto.RefreshToken.SerializedNames.USERNAME;


public class RefreshToken extends Credential {

    public static class SerializedNames extends Credential.SerializedNames {
        /**
         * String of client info.
         */
        public static final String CLIENT_INFO = "client_info";
        /**
         * String of family id.
         */
        public static final String FAMILY_ID = "family_id";
        /**
         * String of target.
         */
        public static final String TARGET = "target";
        /**
         * String of username.
         */
        public static final String USERNAME = "username";
    }

    /**
     * Full base64 encoded client info received from ESTS, if available. STS returns the clientInfo 
     * on both v1 and v2 for AAD. This field is used for extensibility purposes.
     */
    @SerializedName(CLIENT_INFO)
    private String mClientInfo;

    /**
     * 1st Party Application Family ID.
     */
    @SerializedName(FAMILY_ID)
    private String mFamilyId;

    /**
     * Permissions that are included in the token. Formats for endpoints will be different. 
     * <p>
     * Mandatory, if credential is scoped down by some parameters or requirements (e.g. by
     * resource, scopes or permissions).
     */
    @SerializedName(TARGET)
    private String mTarget;

    /**
     * The primary username that represents the user (corresponds to the preferred_username claim
     * in the v2.0 endpoint). It could be an email address, phone number, or a generic username
     * without a specified format. Its value is mutable and might change over time.
     */
    @SerializedName(USERNAME)
    private String mUsername;

    /**
     * Gets the target.
     *
     * @return The target to get.
     */
    public String getTarget() {
        return mTarget;
    }

    /**
     * Sets the target.
     *
     * @param target The target to set.
     */
    public void setTarget(final String target) {
        mTarget = target;
    }

    /**
     * Gets the client_info.
     *
     * @return The client_info to get.
     */
    public String getClientInfo() {
        return mClientInfo;
    }

    /**
     * Sets the client_info.
     *
     * @param clientInfo The clent_info to set.
     */
    public void setClientInfo(final String clientInfo) {
        mClientInfo = clientInfo;
    }

    /**
     * Gets the family_id.
     *
     * @return The family_id to get.
     */
    public String getFamilyId() {
        return mFamilyId;
    }

    /**
     * Sets the family_id.
     *
     * @param familyId The family_id to set.
     */
    public void setFamilyId(final String familyId) {
        mFamilyId = familyId;
    }

    /**
     * Gets the username.
     *
     * @return The username to get.
     */
    public String getUsername() {
        return mUsername;
    }

    /**
     * Sets the username.
     *
     * @param username The username to set.
     */
    public void setUsername(final String username) {
        mUsername = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (!super.equals(o)) {
            return false;
        }

        RefreshToken that = (RefreshToken) o;

        if (mClientInfo != null ? !mClientInfo.equals(that.mClientInfo) : that.mClientInfo != null) {
            return false;
        }

        if (mFamilyId != null ? !mFamilyId.equals(that.mFamilyId) : that.mFamilyId != null) {
            return false;
        }

        if (mTarget != null ? !mTarget.equals(that.mTarget) : that.mTarget != null) {
            return false;
        }

        return mUsername != null ? mUsername.equals(that.mUsername) : that.mUsername == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = UNIQUE_ID_LENGTH * result + (mClientInfo != null ? mClientInfo.hashCode() : 0);
        result = UNIQUE_ID_LENGTH * result + (mFamilyId != null ? mFamilyId.hashCode() : 0);
        result = UNIQUE_ID_LENGTH * result + (mTarget != null ? mTarget.hashCode() : 0);
        result = UNIQUE_ID_LENGTH * result + (mUsername != null ? mUsername.hashCode() : 0);
        return result;
    }
}
/**
 * SponsorPay Android SDK
 *
 * Copyright 2011 - 2014 SponsorPay. All rights reserved.
 */

package com.sponsorpay.sdk.android.credentials;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.sponsorpay.sdk.android.utils.StringUtils;
import com.sponsorpay.sdk.android.utils.UserId;

import java.util.UUID;

/**
 * <p>
 * Object that holds the information about the current user, application and device.
 * </p>
 * <p>
 * The application and user id are immutable. You'll need to create a new credentials object
 * to change any of those.
 * </p>
 */
public class SPCredentials implements Parcelable {
  private String mCredentialsToken;
  private String mAppId;
  private String mUserId;
  private String mSecurityToken;

  public static final Creator<SPCredentials> CREATOR;

  static {
    CREATOR = new Creator<SPCredentials>() {

      @Override
      public SPCredentials createFromParcel(Parcel source) {
        return new SPCredentials(source);
      }

      @Override
      public SPCredentials[] newArray(int size) {
        return new SPCredentials[size];
      }
    };
  }

  public SPCredentials() {
    mCredentialsToken = "";
  }

  public SPCredentials(String appId, String userId, String securityToken, Context context) {
    if (StringUtils.nullOrEmpty(appId)) {
      throw new IllegalArgumentException("AppID cannot be null!");
    }
    mSecurityToken = StringUtils.trim(securityToken);
    mAppId = StringUtils.trim(appId);
    mCredentialsToken = getCredentialsToken(mAppId, userId);
    if (StringUtils.nullOrEmpty(userId)) {
      mUserId = UserId.getAutoGenerated(context);
    } else {
      mUserId = userId;
    }
  }

  private SPCredentials(Parcel source) {
    this();
    readFromParcel(source);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mCredentialsToken);
    dest.writeString(mAppId);
    dest.writeString(mUserId);
    dest.writeString(mSecurityToken);
  }

  private void readFromParcel(Parcel source) {
    mCredentialsToken = source.readString();
    mAppId = source.readString();
    mUserId = source.readString();
    mSecurityToken = source.readString();
  }

  /**
   * Returns the credentials token ID.
   *
   * @return
   */
  public String getCredentialsToken() {
    return mCredentialsToken;
  }

  /**
   * Returns the application ID
   *
   * @return
   */
  public String getAppId() {
    return mAppId;
  }

  /**
   * Returns the user ID (can be a generated one, if none
   * was provided at construction time)
   *
   * @return
   */
  public String getUserId() {
    return mUserId;
  }

  /**
   * Returns the security token
   *
   * @return
   */
  public String getSecurityToken() {
    return mSecurityToken;
  }

  /**
   * Sets a new security token for this credentials object
   *
   * @param securityToken the new security token to be used in this credentials
   */
  public void setSecurityToken(String securityToken) {
    mSecurityToken = securityToken;
  }

  /**
   * Convenience method to get a credentials token id for the appId-userId
   * pair. Throws an {@link IllegalArgumentException} if AppId is null.
   *
   * @param appId  the application id
   * @param userId the user id
   * @return the credentials token
   */
  public static String getCredentialsToken(String appId, String userId) {
    if (StringUtils.nullOrEmpty(appId)) {
      throw new IllegalArgumentException("AppID cannot be null!");
    }
    if (StringUtils.nullOrEmpty(userId)) {
      userId = StringUtils.EMPTY_STRING;
    }
    String token = appId + "-" + userId;
    return UUID.nameUUIDFromBytes(token.getBytes()).toString();
  }

  @Override
  public String toString() {
    return String
        .format("Credentials token - %s\nAppId - %s\nUserId - %s\nSecurityToken - %s",
                mCredentialsToken,
                mAppId,
                StringUtils.notNullNorEmpty(mUserId) ? mUserId : "N/A",
                StringUtils.notNullNorEmpty(mSecurityToken) ? mSecurityToken : "N/A");
  }

}

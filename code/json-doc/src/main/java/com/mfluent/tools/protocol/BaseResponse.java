
package com.mfluent.tools.protocol;

import lombok.Data;

import com.google.gson.annotations.SerializedName;
import com.mfluent.tools.protocol.annotations.Description;

/** TODO: This must be handled differently. It is not suitable for a general purpose tool **/

/**
 * All response objects must be an extension of this BaseResponse
 */
@Data
public class BaseResponse {

    public BaseResponse() {
        super();
    }

    /*
     * they have the transient modifier so that Documentor doesn't list them for every subclass.
     * they have been copied to HttpConstants.ResponseStatusCodes (without the transient modifiers) so that Documentor will list them there.
     * they should be deprecated in favor of the ones in HttpConstatnts.ResponseStatusCodes, which should be made into an enum.
     */
    // TODO: Make these error codes an enum (and move them to HttpConstants (Documentor would like that!))
    public transient static final int SUCCESS = 0;
    public transient static final int FAIL = 1;

    /**
     * Invalid Username or password. This is only valid to send as a response to a login request.
     */
    public transient static final int AUTH_FAILED = 2;

    /**
     * The session key is no longer valid
     */
    public transient static final int SESSION_EXPIRED = 3;

    /**
     * User is trying to perform an action that they do not have the rights to perform
     */
    public transient static final int INSUFFICIENT_RIGHTS = 4;

    /**
     * This happens when the device switches servers or after the DB has been cleared.
     */
    public transient static final int NO_DEVICE_FOR_SESSION = 5;

    /**
     * This happens after the DB has been cleared or if the user's account was deleted.
     */
    public transient static final int NO_USER_FOR_SESSION = 6;

    /**
     * This happens on a create user request or profile update request if the user handle is a duplicate of an already existing one.
     */
    public transient static final int HANDLE_ALREADY_TAKEN = 7;

    /**
     * This happens on a create conversation request or invite to conversation request if number of participants exceeds a system defined maximum.
     */
    public transient static final int MAX_PARTICIPANTS_EXCEEDED = 8;

    /**
     * This happens when a continuation hits a system configured timeout.
     */
    public transient static final int CONTINUATION_TIMEOUT = 9;

    /**
     * This happens when we cannot parse a JSON string.
     */
    public transient static final int BAD_JSON = 10;

    /**
     * This happens when we a request gets mapped to the DefaultServlet.
     */
    public transient static final int BAD_URL = 11;

    /**
     * The access token is no longer valid
     */
    public transient static final int TOKEN_EXPIRED = 12;

    /**
     * The company does not or no longer exists
     */
    public transient static final int BAD_COMPANY = 13;

    /**
     * The username(s) invited are already in the conversation
     */
    public transient static final int INVITEES_ALREADY_INVITED = 14;

    /**
     * The company has exceeded their hard limit space quota
     */
    public transient static final int COMPANY_OVER_SPACE_QUOTA = 15;

    /**
     * The plan does not or no longer exists, or is malformed
     */
    public transient static final int BAD_PLAN = 16;

    /**
     * Tried to change plans to a new plan where the company already exceed the max #users
     */
    public transient static final int TOO_MANY_MEMBERS_FOR_PLAN = 17;

    /**
     * Tried to change plans to a new plan where the company already exceed the max #users
     */
    public transient static final int TOO_MUCH_STORAGE_FOR_PLAN = 18;

    /**
     * Tried to add a user but the company is at the plan limit for max #users
     */
    public transient static final int COMPANY_OVER_USER_QUOTA = 19;

    /**
     * Tried a request that included a username, and the username does not exist
     */
    public transient static final int INVALID_USERNAME = 20;

    /**
     * Tried a request that included a conversation, but the conversation is no longer available
     */
    public transient static final int DEAD_CONVERSATION = 21;

    /**
     * Tried an external request to Stripe that failed unexpectedly
     */
    public transient static final int STRIPE_ERROR = 22;

    /**
     * Tried a Stripe plan that failed with HTTP 400 BAD REQUEST. Commonly this is a duplicate insert.
     */
    public transient static final int STRIPE_BAD_REQUEST = 23;

    /**
     * Tried to assign a Stripe plan that requires charging to a company, but there is no credit card info.
     */
    public transient static final int STRIPE_CC_REQUIRED = 24;

    /**
     * Tried to assign a credit card to the Strip customer account, and Stripe reported failure.
     */
    public transient static final int STRIPE_CC_FAILED = 25;

    /**
     * Tried to log in, but the company is marked disabled.
     */
    public transient static final int COMPANY_DISABLED = 26;

    /**
     * Tried to create a user, but the user already exists.
     */
    public transient static final int USER_ALREADY_EXISTS = 27;

    /**
     * Tried to change plan, but the company owner has not verified their email address.
     */
    public transient static final int EMAIL_VERIFICATION_REQUIRED = 28;

    /**
     * The device missed an ADMINISTRATIVE_WIPE notification
     */
    public transient static final int WIPE_DEVICE = 29;

    /**
     * For clients to use in their HTTP transports
     */
    public transient static final int HTTP_ERROR = -999;

    @SerializedName("ts")
    @Description(value = "The time the server received the request that generated this response (milliseconds since The Epoch)", sample = "1391114519675")
    private Long timeStamp;

    @SerializedName("rt")
    @Description(value = "The response time of the server to the request that generated this response (milliseconds)", sample = "137")
    private Long responseTime;

    @SerializedName("status")
    @Description(value = "The status of the request. See HttpConstants.ResponseStatusCodes for values.", sample = "0")
    private int status = FAIL;

    @SerializedName("msg")
    @Description(value = "An explanation of the status, if not SUCCESS", sample = {})
    private String statusMessage = "";

    @SerializedName("srvid")
    @Description(value = "The id of the server that serviced the request", sample = {"[opaque value]"})
    //private final String serverId;

    private transient static String baseServerId = null;

    public int getStatus() {
        return this.status;
    }

    /**
     * convenience method to check if getStatus() == SUCCESS
     */
    public boolean isSuccess() {
        return getStatus() == SUCCESS;
    }

    public String baseResponseString() {
        return "status=" + getStatus() + ", msg=" + getStatusMessage();
    }

    @Override
    public String toString() {
        return "BaseResponse[status=" + getStatus() + ", msg=" + getStatusMessage() + "]";
    }

    public static String getBaseServerId() {
        return baseServerId;
    }

    public static void setBaseServerId(String baseServerId) {
        BaseResponse.baseServerId = baseServerId;
    }
}

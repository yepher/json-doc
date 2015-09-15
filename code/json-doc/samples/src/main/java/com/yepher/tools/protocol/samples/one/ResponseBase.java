package com.yepher.tools.protocol.samples.one;

import lombok.Data;

import com.google.gson.annotations.SerializedName;
import com.yepher.tools.protocol.annotations.Description;

/**
 * All response objects must be an extension of this BaseResponse
 */
@Data
public class ResponseBase {

    public ResponseBase() {
        super();
    }

    public enum Status {
        /* @formatter:off */
    	SUCCESS(                       0),
    	GENERAL_FAILURE(               1),
    	BAD_REQUEST(                   2),
    	INVALID_USERNAME_OR_PASSWORD(  3),
    	EMAIL_VERIFICATION_REQUIRED(   4),
    	INVALID_AUTHORIZATION(         5),
    	AUTHORIZATION_EXPIRED(         6),
    	/* @formatter:on */
        ;

        public final int code;

        private Status(int code) {
            this.code = code;
        }

    }

    @SerializedName("ts")
    @Description(value = "The time the server received the request that generated this response (milliseconds since The Epoch)", sample = "1391114519675")
    private Long timeStamp;

    @SerializedName("rt")
    @Description(value = "The response time of the server to the request that generated this response (milliseconds)", sample = "137")
    private Long responseTime;

    @SerializedName("status")
    @Description(value = "The status of the request. See BaseResponse.Status for values.", sample = "0")
    private int  statusCode = Status.GENERAL_FAILURE.code;

    /**
     * convenience method to check if getStatusCode() == SUCCESS.code
     */
    public boolean isSuccess() {
        return getStatusCode() == Status.SUCCESS.code;
    }

    public String responseBaseString() {
        return "ts=" + getTimeStamp() + ", rt=" + getResponseTime() + ", statusCode=" + getStatusCode();
    }

    @Override
    public String toString() {
        return "BaseResponse[" + responseBaseString() + "]";
    }

}

package com.meancat.jac;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SomeResponse {
    //{"success":false,"result_code":"ERROR","result_message":"Deserialization error while parsing input."}
    public boolean success;
    @JsonProperty("result_code")
    public String resultCode;
    @JsonProperty("result_message")
    public String resultMessage;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SomeResponse{");
        sb.append("success=").append(success);
        sb.append(", resultCode='").append(resultCode).append('\'');
        sb.append(", resultMessage='").append(resultMessage).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

package com.sajith.flowcontroller.message.flow.decoder;

import com.sajith.flowcontroller.message.flow.Message;
import com.sajith.flowcontroller.message.flow.exceptions.InvalidMessageFormatException;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonMessageDecoder extends MessageDecoder {
    @Override
    public Message decode(String payload) throws InvalidMessageFormatException {
        try {
            JSONObject jsonObject = new JSONObject(payload);
            String senderId = jsonObject.getString(Message.SENDER_ID_FIELD_NAME);
            String functionId = jsonObject.getString(Message.FUNCTION_ID_FIELD_NAME);
            String messageId = jsonObject.getString(Message.MESSAGE_ID_FIELD_NAME);

            return new Message(senderId, functionId, messageId, payload);
        } catch (JSONException e) {
            throw new InvalidMessageFormatException("JSON message is invalid", e);
        }
    }
}

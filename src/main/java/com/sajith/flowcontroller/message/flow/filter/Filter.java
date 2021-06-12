package com.sajith.flowcontroller.message.flow.filter;

import com.sajith.flowcontroller.message.flow.Message;

public interface Filter {
    boolean isAllowed(Message message);
    String getFilteredOutReason();
}

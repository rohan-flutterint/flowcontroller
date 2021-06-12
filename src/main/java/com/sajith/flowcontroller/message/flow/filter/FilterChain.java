package com.sajith.flowcontroller.message.flow.filter;

import com.sajith.flowcontroller.message.flow.MessageProcessor;
import com.sajith.flowcontroller.message.flow.Message;
import com.sajith.flowcontroller.message.flow.exceptions.MessageFilteredOutException;

import java.util.ArrayList;
import java.util.List;

public class FilterChain extends MessageProcessor {
    private List<Filter> filters;

    public FilterChain(List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public void handleMessage(Message message)
            throws MessageFilteredOutException {

        for (Filter filter: filters) {
            if (!filter.isAllowed(message)) {
                throw new MessageFilteredOutException("Message filtered out. Reason :" + filter.getFilteredOutReason());
            }
        }
    }

    public static class FilterChainBuilder {
        private List<Filter> filters;
        private MessageProcessor nextProcessor;

        public FilterChainBuilder(){
            this.filters = new ArrayList<>();
        }

        public FilterChainBuilder filter(Filter filter){
            filters.add(filter);
            return this;
        }

        private void validate() {
        }

        public FilterChain build() {
            FilterChain filterChain = new FilterChain(filters);
            validate();
            return filterChain;
        }
    }

}

package com.opencloud.msg.provider.exchanger;


import com.opencloud.msg.client.model.Notify;

/**
 * @author woodev
 */

public interface MessageExchanger {

    boolean support(Object message);

    boolean exchange(Notify notify);
}

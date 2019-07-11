package com.opencloud.msg.server.task;

import com.opencloud.msg.client.model.BaseMessage;
import com.opencloud.msg.server.exchanger.MessageExchanger;

import java.util.concurrent.Callable;

/**
 * @author woodev
 */
public class NotifyTask implements Callable<Boolean> {

    private MessageExchanger exchanger;

    private BaseMessage notify;

    public NotifyTask(MessageExchanger exchanger, BaseMessage notify){
        this.exchanger = exchanger;
        this.notify = notify;
    }

    @Override
    public Boolean call() throws Exception {
        return exchanger.exchange(notify);
    }
}

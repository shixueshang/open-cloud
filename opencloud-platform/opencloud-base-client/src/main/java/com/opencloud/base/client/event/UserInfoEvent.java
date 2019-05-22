package com.opencloud.base.client.event;

import com.opencloud.base.client.model.entity.BaseUser;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

public class UserInfoEvent extends RemoteApplicationEvent {

    /**
     * 必须要有空参构造
     */
    public UserInfoEvent() {
    }

    /**
     * 重写构造函数
     *
     * @param user 发生事件的对象
     */
    public UserInfoEvent(BaseUser user, String originService, String destinationService) {
        super(user, originService, destinationService);
    }
}

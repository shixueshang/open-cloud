package com.opencloud.base.client.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.opencloud.base.client.model.entity.BaseUser;
import lombok.Getter;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type") //序列化时使用子类的名称作为type
@JsonIgnoreProperties("source") //序列化时，忽略 source
public class UserInfoEvent extends RemoteApplicationEvent {

    //注册用户对象
    private BaseUser user;

    /**
     * 重写构造函数
     *
     * @param source 发生事件的对象
     * @param user   注册用户对象
     */
    public UserInfoEvent(Object source, String originService, String destinationService, BaseUser user) {
        super(source, originService, destinationService);
        this.user = user;
    }
}

package com.github.lyd.base.client.model;

import com.github.lyd.base.client.model.entity.BaseResourceOperation;
import com.github.lyd.base.client.model.entity.BaseResourceMenu;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuyadu
 */
public class BaseResourceMenuDto extends BaseResourceMenu implements Serializable {

    private static final long serialVersionUID = 3474271304324863160L;

    private List<BaseResourceOperation> actionList;

    public List<BaseResourceOperation> getActionList() {
        return actionList;
    }

    public void setActionList(List<BaseResourceOperation> actionList) {
        this.actionList = actionList;
    }
}

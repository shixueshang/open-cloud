package com.github.lyd.base.provider.component;

import com.github.lyd.base.client.model.BaseAuthorityDto;

import java.util.Comparator;

public class AuthorityComparator implements Comparator<BaseAuthorityDto> {

    @Override
    public int compare(BaseAuthorityDto o1, BaseAuthorityDto o2) {
        int diff = (o1.getAuthorityId().intValue()+o1.getPriority()) - (o1.getAuthorityId().intValue()+o2.getPriority());
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        }
        return 0; //相等为0
    }
}

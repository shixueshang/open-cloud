package com.opencloud.common.utils;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.common.exception.OpenAlertException;
import com.opencloud.common.model.ResultBody;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * API 断言
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiAssert {

    /**
     * obj1 eq obj2
     *
     * @param obj1
     * @param obj2
     * @param j
     */
    public static void eq(String info, Object obj1, Object obj2) {
        if (!Objects.equals(obj1, obj2)) {
            failure(info);
            return;
        }
    }

    public static void isTrue(String info, boolean condition) {
        if (!condition) {
            failure(info);
            return;
        }
    }

    public static void isFalse(String info, boolean condition) {
        if (condition) {
            failure(info);
            return;
        }
    }


    public static void gtzero(String info, int n) {
        if (n == 0) {
            failure(info);
            return;
        }
    }

    public static void lezero(String info, int n) {
        if (n > 0) {
            failure(info);
            return;
        }
    }

    public static void isEmpty(String info, Object obj) {
        if (ObjectUtils.isNotEmpty(obj)) {
            failure(info);
            return;
        }
    }

    public static void isNotEmpty(String info, Object obj) {
        if (ObjectUtils.isEmpty(obj)) {
            failure(info);
            return;
        }
    }

    public static void anyOneIsNull(String info, Object... conditions) {
        if (allNotNull(conditions)) {
            failure(info);
            return;
        }
    }

    public static void allNotNull(String info, Object... conditions) {
        if (anyOneIsNull(conditions)) {
            failure(info);
            return;
        }
    }

    /**
     * <p>
     * 失败结果
     * </p>
     *
     * @param j 异常错误码
     */
    private static void failure(ResultBody j) {
        throw new OpenAlertException(1001, j.getMessage());
    }

    /**
     * <p>
     * 失败结果
     * </p>
     */
    public static void failure(String info) {
        throw new OpenAlertException(1001, info);
    }

    public static void notEmpty(ResultBody j, Object[] array) {
        if (ArrayUtils.isEmpty(array)) {
            failure(j);
            return;
        }
    }

    /**
     *
     */
    public static void noNullElements(ResultBody j, Object[] array) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    failure(j);
                    return;
                }
            }
        }
    }

    public static void notEmpty(ResultBody j, Collection<?> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            failure(j);
            return;
        }
    }

    /**
     * 其中一个为空
     *
     * @param objs
     * @return
     */
    public static boolean anyOneIsNull(Object... objs) {
        Object[] var1 = objs;
        int var2 = objs.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Object obj = var1[var3];
            if (isEmpty(obj)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        } else if ((obj instanceof String)) {
            return ((String) obj).trim().equals("");
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else {
            return obj instanceof Map ? ((Map) obj).isEmpty() : false;
        }
    }


    /**
     * 都不为空
     *
     * @param obj
     * @return
     */
    public static boolean allNotNull(Object... obj) {
        return !anyOneIsNull(obj);
    }


}

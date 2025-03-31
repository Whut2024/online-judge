package com.whut.onlinejudge.common.model.enums;


import com.whut.onlinejudge.common.model.entity.User;
import lombok.Getter;

/**
 * 用户角色枚举
 */
@Getter
public enum UserRoleEnum {

    USER("用户", "user"),
    ADMIN("管理员", "admin"),
    BAN("被封号", "ban");

    private final String text;

    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }


    public static boolean isAdmin(User user) {
        if (user == null)
            return false;

        return ADMIN.value.equals(user.getUserRole());
    }

    public static boolean exists(String value) {
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
            if (anEnum.value.equals(value)) {
                return true;
            }
        }
        return false;
    }
}

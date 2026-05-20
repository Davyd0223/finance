package com.javaapp.finance.web.mapper;

import com.javaapp.finance.dto.ProfileTo;
import com.javaapp.finance.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProfileMapper {

    public static ProfileTo toDto(User user) {
        ProfileTo profileTo = new ProfileTo();
        profileTo.setId(user.getId());
        profileTo.setName(user.getName());
        profileTo.setEmail(user.getEmail());
        profileTo.setDefaultCurrency(user.getDefaultCurrency());
        profileTo.setDefaultMonthlyBudget(user.getDefaultMonthlyBudget());
        return profileTo;
    }
}

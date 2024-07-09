package com.phenikaa.jobhuntly.event;

import com.phenikaa.jobhuntly.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ForgotPasswordEvent extends ApplicationEvent {
    public ForgotPasswordEvent(User user) {
        super(user);
    }
}

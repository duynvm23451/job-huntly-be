package com.phenikaa.jobhuntly.event;

import com.phenikaa.jobhuntly.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private String appUrl;

    public RegistrationCompleteEvent(User user, String appUrl) {
        super(user);
        this.appUrl = appUrl;
    }
}

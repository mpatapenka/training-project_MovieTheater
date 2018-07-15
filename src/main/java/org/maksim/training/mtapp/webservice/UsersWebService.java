package org.maksim.training.mtapp.webservice;

import org.maksim.training.mtapp.model.User;
import org.maksim.training.mtapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class UsersWebService {
    private static final String NAMESPACE_URI = "http://mtapp.org/ws";

    private final UserService userService;

    @Autowired
    public UsersWebService(UserService userService) {
        this.userService = userService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "user")
    @ResponsePayload
    public User getUser(@RequestPayload User user) {
        return userService.getById(user.getId());
    }
}
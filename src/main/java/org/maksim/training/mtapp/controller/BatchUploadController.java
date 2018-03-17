//package org.maksim.training.mtapp.controller;
//
//import by.epam.maksim.movietheater.entity.Event;
//import by.epam.maksim.movietheater.entity.User;
//import by.epam.maksim.movietheater.util.JsonParser;
//import com.google.gson.reflect.TypeToken;
//import org.maksim.training.mtapp.entity.Event;
//import org.maksim.training.mtapp.entity.User;
//import org.maksim.training.mtapp.service.EventService;
//import org.maksim.training.mtapp.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.List;
//
//@Controller
//@RequestMapping("/upload/batch")
//public class BatchUploadController {
//    @Autowired
//    private EventService eventService;
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/events")
//    public String uploadEvents(@RequestParam MultipartFile events) {
//        try {
//            List<Event> eventList = JsonParser.convertJsonStringToObject(
//                    new BufferedReader(new InputStreamReader(events.getInputStream())),
//                    new TypeToken<List<Event>>() { }.getType());
//            eventList.forEach(eventService::save);
//        } catch (IOException e) {
//            throw new RuntimeException("Events can't be read.", e);
//        }
//        return "redirect:/";
//    }
//
//    @PostMapping("/users")
//    public String uploadUsers(@RequestParam MultipartFile users) {
//        try {
//            List<User> userList = JsonParser.convertJsonStringToObject(
//                    new BufferedReader(new InputStreamReader(users.getInputStream())),
//                    new TypeToken<List<User>>() { }.getType());
//            userList.forEach(userService::save);
//        } catch (IOException e) {
//            throw new RuntimeException("Users can't be read.", e);
//        }
//        return "redirect:/";
//    }
//}
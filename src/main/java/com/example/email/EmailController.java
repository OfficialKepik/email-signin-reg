package com.example.email;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmailController {
    @Autowired
    EmailService emailService;
    String generatedString;

    @RequestMapping("/sendtext")
    public String sendText() {
        Email email = new Email();
        email.from = "officialkepik@gmail.com";
        email.to = "officialkepik@gmail.com";
        email.subject = "Test #1";
        email.text = "Текст сообщения который написан для теста.";
        emailService.sendTextEmail(email);
        return "success.html";
    }

    @RequestMapping("/sendhtml")
    public String sendHtml() {
        Email email = new Email();
        email.from = "officialkepik@gmail.com";
        email.to = "officialkepik@gmail.com";
        email.subject = "Test #1";
        email.text = "Текст сообщения который написан для теста.";
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", "Name");
        properties.put("subscriptionDate", LocalDate.now().toString());
        properties.put("technologies", Arrays.asList("Java", "Python"));
        email.setProperties(properties);
        emailService.sendHtmlEmail(email);
        return "success.html";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "signup.html";
    }

    @PostMapping("/register")
    public String register(User user) throws NoSuchAlgorithmException {
        generatedString = genHash(user);
        Email email = new Email();
        email.from = "officialkepik@gmail.com";
        email.to = user.getEmail();
        email.subject = "Регистрация";
        email.template = "template.html";
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", user.getName());
        properties.put("subscriptionDate", LocalDate.now().toString());
        properties.put("technologies", Arrays.asList("Java", "Python", "JavaScript"));
        properties.put("hash", generatedString);
        email.setProperties(properties);
        emailService.sendHtmlEmail(email);

        return "success.html";
    }

    @GetMapping("/enable")
    public String enable(Model model, @RequestParam(name = "hash") String hash) {
        System.out.println(generatedString);
        System.out.println(hash);
        if (hash.equals(generatedString))
            System.out.println("success");

        return "signup.html";
    }

    public String genHash(User user) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(user.getPassword().getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}

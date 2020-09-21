package com.spring.cosmos.ebookstore.controller.customer;

import com.azure.cosmos.models.PartitionKey;
import com.spring.cosmos.ebookstore.model.user.Name;
import com.spring.cosmos.ebookstore.model.user.User;
import com.spring.cosmos.ebookstore.model.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class CustomerController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/ebooks/user/createAccount")
    public String createAccount(@ModelAttribute CustomerForm userForm, Model model) {
       String password = this.passwordEncoder.encode(userForm.getPassword());
      User user = new User(userForm.getEmail(),password, new Name(userForm.getFirstName(),userForm.getLastName()));
      if(userRepository.findById(userForm.getEmail(), new PartitionKey(userForm.getEmail())).isPresent()){
          logger.info(userForm.getEmail()+ "is already associated with an account");
          model.addAttribute("accountCreationFailed", "Try another email ID");
          return "createaccount";
      }
      userRepository.save(user);
      return "login";
    }
}

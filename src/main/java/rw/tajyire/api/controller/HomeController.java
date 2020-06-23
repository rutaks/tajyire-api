package rw.tajyire.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

  @GetMapping("/hello")
  public String home(){
    return "Hello";
  }

  @GetMapping("/user")
  public String user(){
    return "Hello User";
  }

  @GetMapping("/admin")
  public String admin() {
    return "Hello admin";
  }
}

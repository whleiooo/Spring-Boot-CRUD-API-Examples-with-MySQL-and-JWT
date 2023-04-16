package net.javaguides.springboot.springsecurity.controller;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import net.javaguides.springboot.springsecurity.service.CustomUserDetailsService;
//import net.javaguides.springboot.springsecurity.util.JwtUtil;
//
//@RestController
//@RequestMapping("/login")
//public class LoginController {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
//
//    @PostMapping
//    public ResponseEntity<String> authenticate(@RequestBody UserCredentials userCredentials) {
//        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userCredentials.getUsername());
//
//        if (!userDetails.getPassword().equals(userCredentials.getPassword())) {
//            return new ResponseEntity<>("Invalid username or password.", HttpStatus.UNAUTHORIZED);
//        }
//
//        final String jwt = jwtUtil.generateToken(userDetails);
//        return ResponseEntity.ok(jwt);
//    }
//
//    // Create a new static class for the request body
//    public static class UserCredentials {
//    	private String username;
//        private String password;
//        
//        public String getUsername() {
//			return username;
//		}
//		public void setUsername(String username) {
//			this.username = username;
//		}
//		public String getPassword() {
//			return password;
//		}
//		public void setPassword(String password) {
//			this.password = password;
//		}
//    }
//}

import net.javaguides.springboot.springsecurity.service.CustomUserDetailsService;
import net.javaguides.springboot.springsecurity.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String showLoginForm() {
    	 return "login";
    }
    
    @GetMapping("/index")
    public String userIndex() {
    	 return "index";
    }

    @PostMapping("/login")
    public String submitLoginForm(HttpServletRequest request, HttpSession session) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwtToken = jwtUtil.generateToken(userDetails);
            session.setAttribute("jwtToken", jwtToken);

            return "redirect:/index"; // redirect to the appropriate page
        } else {
            return "redirect:/login?error=true"; // redirect to login with error flag
        }
    }
}


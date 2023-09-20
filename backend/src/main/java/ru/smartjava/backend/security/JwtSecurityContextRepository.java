//package ru.smartjava.backend.security;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.context.HttpRequestResponseHolder;
//import org.springframework.security.web.context.SecurityContextRepository;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JwtSecurityContextRepository implements SecurityContextRepository {
//
//    private UserDetailsService userDetailsService;
//
//    @Override
//    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
//        // Extract the JWT token from the request
//        HttpServletRequest request = requestResponseHolder.getRequest();
//        Optional<String> maybeToken = JwtUtils.getToken(request);
//
//        // Get the security context for the request thread, which should be empty at this point
//        SecurityContext context = SecurityContextHolder.getContext();
//
//        // If token doesn't exist, return the empty context
//        if (maybeToken.isEmpty()) {
//            return context;
//        }
//
//        // decode and validate the JWT
//        Optional<DecodedJWT> decodedJWT = JwtUtils.getValidatedToken(maybeToken.get());
//        // if the token cannot be decoded or validated, return the empty context
//        if (decodedJWT.isEmpty()) {
//            return context;
//        }
//
//        // Create the UserDetails instance from the decoded JWT
//        UserDetails userDetails = userDetailsService
//                .loadUserByUsername(decodedJWT.get().getClaim("username").asString());
//
//        // Create the authentication token from userDetails and add it to the security context
//        // This will now be available in all downstream spring filters (including the request
//        // handlers)
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                new UsernamePasswordAuthenticationToken(userDetails, null,
//                        userDetails.getAuthorities());
//        context.setAuthentication(usernamePasswordAuthenticationToken);
//        return context;
//    }
//
//    @Override
//    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
//
//    }
//
//    // to override this method, we need to return a boolean value denoting
//    // if the token exists in our request
//    @Override
//    public boolean containsContext(HttpServletRequest request) {
//        return JwtUtils.getToken(request).isPresent();
//    }
//
//}

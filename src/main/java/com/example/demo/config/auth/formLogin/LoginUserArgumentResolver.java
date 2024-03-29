//package com.example.demo.config.auth;
//import com.example.demo.config.auth.dto.SessionUser;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.MethodParameter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//import javax.servlet.http.HttpSession;
//
//@RequiredArgsConstructor
//@Component
//public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
//    private final HttpSession httpSession;
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
//        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
//        System.out.println("--------------------------------------------------");
//        System.out.println("isLoginUserAnnotation: "+isLoginUserAnnotation+" isUserClass: "+isUserClass);
//        return isLoginUserAnnotation && isUserClass;
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        System.out.println("--------------------------------------------------");
//        System.out.println("session에 값 저장");
//        return httpSession.getAttribute("user");
//    }
//}

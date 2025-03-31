package com.demo.variety_store_mono.admin.resolver;

import com.demo.variety_store_mono.admin.dto.request.AdminDetailRequest;
import com.demo.variety_store_mono.common.request.UserBasicInfoRequest;
import com.demo.variety_store_mono.customer.request.CustomerDetailRequest;
import com.demo.variety_store_mono.seller.dto.request.SellerDetailRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserDetailRequestResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // UserBasicInfoRequest 를 구현/상속 받은 타입에 대해 처리
        boolean isType = UserBasicInfoRequest.class.isAssignableFrom(parameter.getParameterType());
        // 스프링 기본 ArgumentResolver 에 의한 변환 방지.
        boolean hasAnnotation = parameter.hasParameterAnnotation(UserDetail.class);

        return isType && hasAnnotation;
    }


    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        // 요청 파라미터 "userType"를 확인합니다.
        String userTypeStr = webRequest.getParameter("userType");
        if(userTypeStr == null) {
            throw new IllegalArgumentException("userType 파라미터가 필요합니다.");
        }

        // userType에 따라 구체적인 DTO 인스턴스를 생성합니다.
        UserBasicInfoRequest target;
        switch(userTypeStr.toUpperCase()) {
            case "ADMIN":
                target = new AdminDetailRequest();
                break;
            case "SELLER":
                target = new SellerDetailRequest();
                break;
            case "CUSTOMER":
                target = new CustomerDetailRequest();
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 userType: " + userTypeStr);
        }

        // WebDataBinder를 생성하여 요청 파라미터를 target 객체에 바인딩합니다.
        WebDataBinder binder = binderFactory.createBinder(webRequest, target, parameter.getParameterName());
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        binder.bind(new MutablePropertyValues(request.getParameterMap()));

        return target;
    }
}

    package com.example.cargo.aspect;

    import com.example.cargo.annotation.RequireRole;
    import com.example.cargo.model.Role;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpSession;
    import org.aspectj.lang.ProceedingJoinPoint;
    import org.aspectj.lang.annotation.Around;
    import org.aspectj.lang.annotation.Aspect;
    import org.aspectj.lang.reflect.MethodSignature;
    import org.springframework.stereotype.Component;
    import org.springframework.web.context.request.RequestContextHolder;
    import org.springframework.web.context.request.ServletRequestAttributes;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    @Aspect
    @Component
    public class RoleCheckAspect {

        @Around("@annotation(requireRole)")
        public Object checkRole(ProceedingJoinPoint joinPoint, RequireRole requireRole) throws Throwable {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            HttpSession session = request.getSession();
            Role userRole = (Role) session.getAttribute("role");

            if (userRole == null || userRole != requireRole.value()) {
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Class<?> returnType = signature.getReturnType();

                Object[] args = joinPoint.getArgs();
                RedirectAttributes redirectAttributes = null;
                for (Object arg : args) {
                    if (arg instanceof RedirectAttributes) {
                        redirectAttributes = (RedirectAttributes) arg;
                        break;
                    }
                }

                String errorMessage = "У вас нет прав для выполнения этого действия";

                if (returnType.equals(ModelAndView.class)) {
                    ModelAndView mav = new ModelAndView("error");
                    mav.addObject("errorMessage", errorMessage);
                    return mav;
                } else if (returnType.equals(String.class)) {
                    if (redirectAttributes != null) {
                        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                    }
                    return "redirect:/access-denied";
                }
                return null;
            }

            return joinPoint.proceed();
        }
    }


package com.itheima.hchat.filter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EnhanceMonitor {

    //定义切点
    @Pointcut("execution(* com.itheima.hchat.filter.*..attack(..))")
    public void pointCutMethod(){}

    //前置
    @Before("pointCutMethod()")
    public void magicEnhance1(){
        System.out.println("----冰霜特效----");
    }

    //前置
    @Before("pointCutMethod()")
    public void magicEnhance2(){
        System.out.println("----火焰特效----");
    }

    //后置
    @After("pointCutMethod()")
    public void afterAttack(){
        System.out.println("---攻击后僵直---");
    }

    /**环绕
     *
     * @param pjp
     * @throws Throwable
     */
    @Around("pointCutMethod()")
    public void buffAttack(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("----骑士光环---");
        pjp.proceed();
        System.out.println("-----骑士光环消失----");
    }
}

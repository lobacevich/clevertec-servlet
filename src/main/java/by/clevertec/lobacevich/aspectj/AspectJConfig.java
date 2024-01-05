package by.clevertec.lobacevich.aspectj;

import by.clevertec.lobacevich.cache.Cache;
import by.clevertec.lobacevich.dto.UserDto;
import by.clevertec.lobacevich.entity.User;
import by.clevertec.lobacevich.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class AspectJConfig {

    private final Cache cache;
    private final Validator validator;

    @Around("execution(* by.clevertec.lobacevich.dao.impl.UserDaoImpl.findUserById(..))")
    public Object daoFindById(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        User cacheUser = cache.getById((Long) args[0]);
        if (cacheUser != null) {
            return Optional.of(cacheUser);
        } else {
            Optional<User> result = (Optional<User>) joinPoint.proceed();
            result.ifPresent(cache::put);
            return result;
        }
    }

    @Around("execution(* by.clevertec.lobacevich.dao.impl.UserDaoImpl.createUser(..))")
    public Object daoCreateUser(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        cache.put((User) result);
        return result;
    }

    @Around("execution(* by.clevertec.lobacevich.dao.impl.UserDaoImpl.updateUser(..))")
    public Object daoUpdateUser(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        cache.put((User) args[0]);
        return joinPoint.proceed();
    }

    @Around("execution(* by.clevertec.lobacevich.dao.impl.UserDaoImpl.deleteUser(..))")
    public Object daoDeleteUser(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object result = joinPoint.proceed();
        cache.deleteById((Long) args[0]);
        return result;
    }

    @Around("execution(* by.clevertec.lobacevich.service.impl.UserServiceImpl.createUser(..))")
    public Object serviceCreateUser(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        validator.validateToCreate((UserDto) args[0]);
        return joinPoint.proceed();
    }

    @Around("execution(* by.clevertec.lobacevich.service.impl.UserServiceImpl.updateUser(..))")
    public Object serviceUpdateUser(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        validator.validateToUpdate((UserDto) args[0]);
        return joinPoint.proceed();
    }

    @Around("execution(* by.clevertec.lobacevich.service.impl.UserServiceImpl.findUserById(..))")
    public Object serviceFindUserById(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}

package by.clevertec.lobacevich.aspectj;

import by.clevertec.lobacevich.cache.Cache;
import by.clevertec.lobacevich.cache.factory.CacheFactory;
import by.clevertec.lobacevich.cache.factory.impl.LFUCacheFactory;
import by.clevertec.lobacevich.cache.factory.impl.LRUCacheFactory;
import by.clevertec.lobacevich.data.UserDto;
import by.clevertec.lobacevich.entity.User;
import by.clevertec.lobacevich.exception.YamlReaderException;
import by.clevertec.lobacevich.pdf.PdfGenerator;
import by.clevertec.lobacevich.pdf.impl.UserPdfGenerator;
import by.clevertec.lobacevich.util.YamlReader;
import by.clevertec.lobacevich.validator.Validator;
import by.clevertec.lobacevich.validator.impl.UserDtoValidator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Optional;

@Aspect
public class AspectJConfig {

    private final Cache cache = setCache();
    private final Validator validator = UserDtoValidator.getINSTANCE();
    private final PdfGenerator pdfGenerator = UserPdfGenerator.getInstance();

    private Cache setCache() {
        CacheFactory cacheFactory;
        if (YamlReader.getData().get("Cache.algorithm").equals("LRU")) {
            cacheFactory = new LRUCacheFactory();
        } else if (YamlReader.getData().get("Cache.algorithm").equals("LFU")) {
            cacheFactory = new LFUCacheFactory();
        } else {
            throw new YamlReaderException("Can't get cache algorithm");
        }
        return cacheFactory.createCache();
    }

    @Around("execution(* by.clevertec.lobacevich.dao.impl.UserDaoImpl.findUserById(..))")
    public Object findById(ProceedingJoinPoint joinPoint) throws Throwable {
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
        Object result = joinPoint.proceed();
        pdfGenerator.createPdf((UserDto) result);
        return result;
    }

    @Around("execution(* by.clevertec.lobacevich.service.impl.UserServiceImpl.updateUser(..))")
    public Object serviceUpdateUser(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        validator.validateToUpdate((UserDto) args[0]);
        pdfGenerator.createPdf((UserDto) args[0]);
        return joinPoint.proceed();
    }

    @Around("execution(* by.clevertec.lobacevich.service.impl.UserServiceImpl.findUserById(..))")
    public Object serviceFindUserById(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        pdfGenerator.createPdf((UserDto) result);
        return result;
    }
}

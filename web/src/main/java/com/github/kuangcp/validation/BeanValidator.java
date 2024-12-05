package com.github.kuangcp.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 基于注解的校验， @NotBlank @NotEmpty....
 * 其他注解请参考{@link org.hibernate.validator.constraints} 和 {@link javax.validation.constraints}
 */
@Slf4j
public abstract class BeanValidator {

    private static final ValidatorFactory validatorFactory = Validation
            .buildDefaultValidatorFactory();

    /**
     * 校验多个字段
     *
     * @param t      校验的对象
     * @param groups Class, 必须要传，如果没有就传入一个new Class[0]
     * @param <T>    校验的对象泛型
     * @return key: 字段，value：错误信息
     */
    public static <T> Map<String, String> validate(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> violationSet = validator.validate(t, groups);
        if (violationSet.isEmpty()) {
            return Collections.emptyMap();
        } else {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<T> violation : violationSet) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }

    /**
     * 校验多个对象
     *
     * @param collection 集合
     * @return key: 字段，value：错误信息
     */
    public static Map<String, String> validateList(Collection<?> collection) {
        Iterator<?> iterator = collection.iterator();
        Map<String, String> errors;
        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            // new Class[0] 必须要传， 否则在determineGroupValidationOrder方法中，会抛出异常
            errors = validate(object, new Class[0]);
        } while (errors.isEmpty());
        return errors;
    }

    /**
     * 综合validateList 和 validate 方法， 任何校验只需要使用这个方法就可以
     *
     * @param first   第一个对象
     * @param objects 对象数组
     * @return key: 字段，value：错误信息
     */
    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Arrays.asList(first, objects));
        } else {
            // new Class[0] 必须要传， 否则在determineGroupValidationOrder方法中，会抛出异常
            return validate(first, new Class[0]);
        }
    }

    /**
     * 再次封装校验，对于异常直接抛出
     *
     * @param param 参数
     */
    public static void check(Object param) throws IllegalArgumentException {
        Map<String, String> validateMap = BeanValidator.validateObject(param);
        if (Objects.nonNull(validateMap) && !validateMap.isEmpty()) {
            throw new RuntimeException(validateMap.values().iterator().next());
        }
    }
}

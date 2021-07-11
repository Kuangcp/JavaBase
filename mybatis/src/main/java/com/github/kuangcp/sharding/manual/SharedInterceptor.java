package com.github.kuangcp.sharding.manual;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author https://github.com/kuangcp on 2021-07-11 18:19
 */
@Slf4j
@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare",
        args = {Connection.class, Integer.class})})
public class SharedInterceptor implements Interceptor {

    @Autowired
    private AuthUtil authUtil;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MetaObject metaObject;
        MappedStatement mappedStatement;
        Object target = invocation.getTarget();

        if (!(target instanceof RoutingStatementHandler)) {
            return invocation.proceed();
        }
        RoutingStatementHandler routingStatementHandler = (RoutingStatementHandler) target;
        metaObject = SystemMetaObject.forObject(routingStatementHandler);
        StatementHandler statementHandler = (StatementHandler) metaObject.getValue("delegate");
        metaObject = SystemMetaObject.forObject(statementHandler);

        mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
        BoundSql boundSql = statementHandler.getBoundSql();
        //获取对应的Mapper类
        Class<?> mapperClass = Class.forName(mappedStatement.getId().substring(0, mappedStatement.getId().lastIndexOf(".")));
        //获取对应EO
        Class<?> eoClass = getEoClass(mapperClass);
        if (eoClass.isAnnotationPresent(ShardingTable.class) && eoClass.isAnnotationPresent(TableName.class)) {
            String logicTable = eoClass.getAnnotation(TableName.class).value();
            ShardingTable rdsSharding = eoClass.getAnnotation(ShardingTable.class);
            int algorithm = rdsSharding.algorithm();
            int tableTotal = rdsSharding.tableCount();

            Long orgId = authUtil.getAuthedOrgId();

            // 统一使用 组织id 分表或者不分表
            String subTableName = ShardingAlgorithmEnum.of(algorithm)
                    .map(ShardingAlgorithmEnum::getFunc)
                    .map(v -> v.apply(orgId, tableTotal))
                    .map(v -> logicTable + v)
                    .orElse(logicTable);

            if (StringUtils.isEmpty(subTableName)) {
                log.error("Unable to obtain subTableName , exec canceled. caseby: {} splitKey's value is null", logicTable);
            } else {
                String sql = boundSql.getSql();
                //将表名替换为子表名
                sql = sql.replaceAll(logicTable, subTableName);

                metaObject = SystemMetaObject.forObject(boundSql);
                metaObject.setValue("sql", sql);
            }
        }

        return invocation.proceed();
    }


    @Override
    public Object plugin(Object target) {
        // TODO Auto-generated method stub
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 获取Eo class
     *
     * @param eoMapper
     * @return
     */
    private Class<?> getEoClass(Class<?> eoMapper) {
        Class entityClass = getGenericClass(eoMapper);
        if (entityClass != null) {
            String eoName = entityClass.getPackage().getName() + "." + StringUtils.delete(entityClass.getSimpleName(), "Eo") + "ExtEo";

            try {
                Class extClass = Class.forName(eoName);
                entityClass = extClass;
            } catch (ClassNotFoundException exception) {
            }
        }

        return entityClass;
    }

    /**
     * 获取接口的泛型类型，如果不存在则返回null
     *
     * @param clazz
     * @return
     */
    private Class<?> getGenericClass(Class<?> clazz) {
        Type t = clazz.getGenericSuperclass();
        if (t == null) {
            t = clazz.getGenericInterfaces()[0];
        }
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            return ((Class<?>) p[0]);
        }
        return null;
    }
}

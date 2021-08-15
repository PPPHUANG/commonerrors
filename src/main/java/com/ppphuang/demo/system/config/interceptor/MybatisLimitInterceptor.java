package com.ppphuang.demo.system.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.Properties;

@Intercepts(@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
@Slf4j
public class MybatisLimitInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取sql
        String sql = getSqlByInvocation(invocation).replaceAll(" +", " ");
        ;
        if (StringUtils.isEmpty(sql)) {
            return invocation.proceed();
        }

        //判断是否是连表查询
        if (isUnionOrJoin(sql)) {
            throw new MybatisLimitException("不允许使用连表查询！");
        }

        // sql交由处理类处理  对sql语句进行处理
        String sql2Reset = "";
        //不是查询数量的并且没有limit的语句 都追加默认的limit1000
        if (!isSelectCount(sql) && !hasLimit(sql)) {
            sql2Reset = sql + " limit 1000";
            log.error("该SQL自动添加LIMIT 1000, 可能会影响业务，务必重视并解决！！！！：{}", sql);
        }

        // 包装sql后，重置到invocation中
        if (!StringUtils.isEmpty(sql2Reset)) {
            resetSql2Invocation(invocation, sql2Reset);
        }

        // 返回，继续执行
        return invocation.proceed();
    }

    /**
     * 判断sql语句中是否包含自定义字段
     *
     * @param sql sql语句
     * @return
     */
    private boolean hasFieldId(String sql, String[] officeIdNames) {
        if (sql == null || sql.trim().length() == 0) {
            return false;
        }
        String afterWhereStatement = sql.toUpperCase().substring(sql.toUpperCase().indexOf("WHERE"));
        if ("WHERE1=1".equals(afterWhereStatement.replaceAll(" ", ""))) {
            log.error("SQL异常,务必重视并解决！！！：{}", sql);
        }

        for (String officeIdName : officeIdNames) {
            if (afterWhereStatement.indexOf(officeIdName) > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断sql语句中是否包含limit字段
     *
     * @param sql sql语句
     * @return
     */
    private boolean hasLimit(String sql) {
        return hasFieldId(sql, new String[]{"LIMIT"});
    }

    /**
     * 判断sql语句中是否是查询数量的
     *
     * @param sql sql语句
     * @return
     */
    private boolean isSelectCount(String sql) {
        return sql.toUpperCase().indexOf("COUNT(") > 0;
    }

    /**
     * 判断sql语句中是否是连表查询
     *
     * @param sql sql语句
     * @return
     */
    private boolean isUnionOrJoin(String sql) {
        String upperCaseSql = sql.toUpperCase();
        //判断略显草率 可以优化
        return upperCaseSql.indexOf("JOIN") > 0 || upperCaseSql.indexOf("UNION") > 0;
    }

    /**
     * 判断sql语句中是否只有where1=1
     *
     * @param sql sql语句
     * @return
     */
    private boolean onlyWhere11(String sql) {
        if (sql == null || sql.trim().length() == 0) {
            return false;
        }
        String afterWhereStatement = sql.toUpperCase().substring(sql.toUpperCase().indexOf("WHERE"));
        return "WHERE1=1".equals(afterWhereStatement.replaceAll(" ", ""));
    }

    @Override
    public Object plugin(Object obj) {
        return Plugin.wrap(obj, this);
    }

    @Override
    public void setProperties(Properties arg0) {
        // doSomething
    }

    /**
     * 获取sql语句
     *
     * @param invocation
     * @return
     */
    private String getSqlByInvocation(Invocation invocation) {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        return boundSql.getSql();
    }

    /**
     * 包装sql后，重置到invocation中
     *
     * @param invocation
     * @param sql
     * @throws SQLException
     */
    private void resetSql2Invocation(Invocation invocation, String sql) throws SQLException {
        final Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = statement.getBoundSql(parameterObject);
        MappedStatement newStatement = newMappedStatement(statement, new BoundSqlSqlSource(boundSql));
        MetaObject msObject = MetaObject.forObject(newStatement, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
        msObject.setValue("sqlSource.boundSql.sql", sql);
        args[0] = newStatement;
    }

    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder =
                new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    private String getOperateType(Invocation invocation) {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        SqlCommandType commondType = ms.getSqlCommandType();
        if (commondType.compareTo(SqlCommandType.SELECT) == 0) {
            return "select";
        }
        if (commondType.compareTo(SqlCommandType.INSERT) == 0) {
            return "insert";
        }
        if (commondType.compareTo(SqlCommandType.UPDATE) == 0) {
            return "update";
        }
        if (commondType.compareTo(SqlCommandType.DELETE) == 0) {
            return "delete";
        }
        return null;
    }

    //    定义一个内部辅助类，作用是包装sq
    class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}

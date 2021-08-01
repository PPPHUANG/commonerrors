package com.ppphuang.demo.reentrantLock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 改进点
 * 适合读多写少
 * 没有考虑缓存容量、过期时间
 * 只适合单机
 * 并发低，目前只会使用一把锁，可以按不同库、表来使用不同的锁
 * 更新方法太简单，清空所有的key（考虑按类型分区或重新设计key）
 */
public class RWLockDaoCache {
    static final int SHARED_SHIFT = 16;

    public static void main(String[] args) {
        System.out.println(1 << SHARED_SHIFT);
//        GeneriCacheDao<Object>  generiCacheDao = new GeneriCacheDao<>();
//
//        Object[] objects = new Object[2];
//        generiCacheDao.queryOne(Object.class,"Test",objects);
//        generiCacheDao.queryOne(Object.class,"Test",objects);
//        generiCacheDao.queryOne(Object.class,"Test",objects);
//        generiCacheDao.queryOne(Object.class,"Test",objects);
//        System.out.println(generiCacheDao.map);
//        generiCacheDao.update("Test",objects);
//        System.out.println(generiCacheDao.map);
    }
}

class GeneriCacheDao<T> {
    HashMap<SqlPair, T> map = new HashMap<>();

    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    GenericDao genericDao = new GenericDao();


    public int update(String sql, Object... params) {
        SqlPair sqlPair = new SqlPair(sql, params);
        lock.writeLock().lock();
        try {
            // 先查询数据库再更新缓存,但是这里加了锁，谁先谁后都没关系
            int update = genericDao.update(sql, params);
            map.clear();
            return update;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public T queryOne(Class<T> beanClass, String sql, Object... params) {
        SqlPair key = new SqlPair(sql, params);
        // 加读锁, 防止其它线程对缓存更改
        lock.readLock().lock();

        try {
            T t = map.get(key);
            if (t != null) {
                return t;
            }
        } finally {
            lock.readLock().unlock();
        }

        // 加写锁, 防止其它线程对缓存读取和更改

        lock.writeLock().lock();


        // get 方法上面部分是可能多个线程进来的, 可能已经向缓存填充了数据
        // 为防止重复查询数据库, 再次验证
        try {
            T value = map.get(key);
            if (value == null) {
                value = (T) genericDao.queryOne(beanClass, sql, params);
                map.put(key, value);
            }
            return value;
        } finally {
            lock.writeLock().unlock();
        }
    }


    class SqlPair {
        private String sql;

        private Object[] params;

        public SqlPair(String sql, Object[] params) {
            this.sql = sql;
            this.params = params;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SqlPair sqlMap = (SqlPair) o;
            return Objects.equals(sql, sqlMap.sql) &&
                    Arrays.equals(params, sqlMap.params);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(sql);
            result = 31 * result + Arrays.hashCode(params);
            return result;
        }
    }
}

class GenericDao<T> {
    public int update(String sql, Object... params) {
        return 1;
    }

    public T queryOne(Class<T> beanClass, String sql, Object... params) {
        System.out.println("查询数据库中");

        return (T) new Object();
    }
}
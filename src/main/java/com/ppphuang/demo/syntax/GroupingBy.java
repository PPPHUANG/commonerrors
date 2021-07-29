package com.ppphuang.demo.syntax;

import lombok.Data;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GroupingBy {
    public static void main(String[] args) {

        Product prod1 = new Product(1L, 1, new BigDecimal("15.5"), "面包", "零食");
        Product prod2 = new Product(2L, 2, new BigDecimal("20"), "饼干", "零食");
        Product prod3 = new Product(3L, 3, new BigDecimal("30"), "月饼", "零食");
        Product prod4 = new Product(4L, 3, new BigDecimal("10"), "青岛啤酒", "啤酒");
        Product prod5 = new Product(5L, 10, new BigDecimal("15"), "百威啤酒", "啤酒");
        List<Product> prodList = Arrays.asList(prod1, prod2, prod3, prod4, prod5);
        //按照类目分组：
        Map<String, List<Product>> prodMap = prodList.stream().collect(Collectors.groupingBy(Product::getCategory));
        System.out.println(prodMap);

        //按照几个属性拼接分组：
        Map<String, List<Product>> prodMap1 = prodList.stream().collect(Collectors.groupingBy(item -> item.getCategory() + "_" + item.getName()));
        System.out.println(prodMap1);

        //根据不同条件分组
        Map<String, List<Product>> prodMap2 = prodList.stream().collect(Collectors.groupingBy(item -> {
            if (item.getNum() < 3) {
                return "3";
            } else {
                return "other";
            }
        }));
        System.out.println(prodMap2);

        //多级分组
        Map<String, Map<String, List<Product>>> prodMap3 = prodList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.groupingBy(item -> {
            if (item.getNum() < 3) {
                return "3";
            } else {
                return "other";
            }
        })));

        System.out.println("====多级分组=====");
        System.out.println(prodMap3);
        System.out.println("====多级分组=====");


        Map<String, Map<String, Long>> prodMap33 = prodList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.groupingBy(item -> {
            if (item.getNum() < 3) {
                return "3";
            } else {
                return "other";
            }
        }, Collectors.counting())));

        System.out.println("====多级分组求总数=====");
        System.out.println(prodMap33);
        System.out.println("====多级分组求总数=====");

        //按子组收集数据
        //求总数
        Map<String, Long> prodMap4 = prodList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));

        System.out.println("====求总数=====");
        System.out.println(prodMap4);
        System.out.println("====求总数=====");

        //求和
        Map<String, Integer> prodMap5 = prodList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.summingInt(Product::getNum)));
        System.out.println("====求和=====");
        System.out.println(prodMap5);
        System.out.println("====求和=====");

        //把收集器的结果转换为另一种类型
        Map<String, Product> prodMap6 = prodList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(Product::getNum)), Optional::get)));
        System.out.println("====收集器的结果转换为另一种类型=====");
        System.out.println(prodMap6);

        Map<String, Product> prodMap7 = prodList.stream().collect(Collectors.toMap(Product::getCategory, Function.identity(), BinaryOperator.maxBy(Comparator.comparingInt(Product::getNum))));

        System.out.println(prodMap7);
        System.out.println("====收集器的结果转换为另一种类型=====");

        //联合其他收集器
        Map<String, Set<String>> prodMap8 = prodList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.mapping(Product::getName, Collectors.toSet())));
        System.out.println("====联合其他收集器=====");
        System.out.println(prodMap8);
        System.out.println("====联合其他收集器=====");

    }
}

@Data
class Product {
    long id;
    int num;
    BigDecimal price;
    String name;
    String category;

    public Product(long id, int num, BigDecimal price, String name, String category) {
        this.id = id;
        this.num = num;
        this.price = price;
        this.name = name;
        this.category = category;
    }
}
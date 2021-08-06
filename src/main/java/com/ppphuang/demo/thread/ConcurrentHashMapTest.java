package com.ppphuang.demo.thread;


import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 单词统计
 */
public class ConcurrentHashMapTest {
    public static void main(String[] args) {
        //生成需要的文件
        //        inita();

//        demo((Supplier<Map<String, Integer>>) HashMap::new,(map,words)->{
//            for(String word : words) {
//                Integer count = map.get(word);
//                int newInt = count == null ? 1 : count + 1;
//                map.put(word,newInt);
//            }
//        });

//        demo((Supplier<Map<String, Integer>>) ConcurrentHashMap::new,(map,words)->{
//            for(String word : words) {
//                //ConcurrentHashMap的单个操作是线程安全的 但是 map.get map.put连起来不是线程安全的 所以还是会有问题
//                Integer count = map.get(word);
//                int newInt = count == null ? 1 : count + 1;
//                map.put(word,newInt);
//            }
//        });

        demo((Supplier<Map<String, LongAdder>>) ConcurrentHashMap::new, (map, words) -> {
            for (String word : words) {
                // ConcurrentHashMap.computeIfAbsent LongAdder都是线程安全的 所以是正解
                LongAdder longAdder = map.computeIfAbsent(word, (k) -> new LongAdder());
                longAdder.increment();
            }
        });

//        LongAdder longAdder = new LongAdder();
//        LongAdder a1 = map.computeIfAbsent("a", (a) -> longAdder);
//        a1.increment();
//        a1.increment();
//        a1.increment();
//        System.out.println(a1);
    }

    public static void inita() {
        String ALPHA = "abcdefghijklmnopqrstuvwxyz";
        int length = ALPHA.length();
        int count = 200;
        ArrayList<String> objects = new ArrayList<>(length * count);
        for (int i = 0; i < length; i++) {
            char c = ALPHA.charAt(i);
            for (int j = 0; j < count; j++) {
                objects.add(String.valueOf(c));
            }
        }
        Collections.shuffle(objects);
        for (int i = 0; i < 26; i++) {
            try (
                    PrintWriter out = new PrintWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream("/Users/ppphuang/Documents/code/java/src/main/java/com/ppphuang/demo/thread/tmp/" + (i + 1) + ".txt")
                            )
                    )) {
                String collect = String.join("\n", objects.subList(i * count, (i + 1) * count));
                out.println(collect);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * supplier 提供者 无中生有 ()->结果
     * function 函数 一个参数一个结果 (参数)->结果 , BiFunction (参数1,参数2)->结果
     * consumer 消费者 一个参数没结果 (参数)->void, BiConsumer (参数1,参数2)->
     * predicate 是一个判断型接口，boolean test(T t);
     *
     * @param arraySupplier
     * @param consumer
     * @param <T>
     */
    public static <T> void demo(Supplier<Map<String, T>> arraySupplier, BiConsumer<Map<String, T>, List<String>> consumer) {
        Map<String, T> stringMap = arraySupplier.get();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            int idx = i;
            Thread thread = new Thread(() -> {
                List<String> words = readFromFile(idx);
                consumer.accept(stringMap, words);
            });
            threads.add(thread);
        }
        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(stringMap);
    }

    public static List<String> readFromFile(int idx) {
        List<String> words = new ArrayList<>();
        idx++;
        String fileName = "/Users/ppphuang/Documents/code/java/src/main/java/com/ppphuang/demo/thread/tmp/" + idx + ".txt";
        File file = new File(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                words.add(tempString);
                //System.out.println("line " + line + ": " + tempString);
                line++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}

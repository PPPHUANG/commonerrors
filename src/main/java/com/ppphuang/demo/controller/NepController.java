package com.ppphuang.demo.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
public class NepController {
    private List<String> wrongMethod(FoodService foodService, Integer i,String s, String t) {
        log.info("result{}{}{}{}",i+1,s.equals("OK"),s.equals(t),new ConcurrentHashMap<String,String>().put(null,null));
        if (foodService.getBarService().bar().equals("OK")){
            log.info("OK");
        }
        return null;
    }

    @GetMapping("nep/wrong")
    public int wrong(@RequestParam(value = "test",defaultValue = "1111") String test){
        return wrongMethod(test.charAt(0) == '1' ? null : new FoodService(),test.charAt(1)=='1'?null:1,test.charAt(2)=='1'?null:"OK",test.charAt(3)=='1'?null:"OK").size();
    }

    @GetMapping("nep/right")
    public int right(@RequestParam(value = "test",defaultValue = "1111") String test){
        return Optional.ofNullable(rightMethod(test.charAt(0) == '1' ? null : new FoodService(),test.charAt(1)=='1'?null:1,test.charAt(2)=='1'?null:"OK",test.charAt(3)=='1'?null:"OK")).orElse(Collections.emptyList()).size();
    }

//    对于 Integer 的判空，可以使用 Optional.ofNullable 来构造一个 Optional，然后使用 orElse(0) 把 null 替换为默认值再进行 +1 操作。
//    对于 String 和字面量的比较，可以把字面量放在前面，比如"OK".equals(s)，这样即使 s 是 null 也不会出现空指针异常；而对于两个可能为 null 的字符串变量的 equals 比较，可以使用 Objects.equals，它会做判空处理。
//    对于 ConcurrentHashMap，既然其 Key 和 Value 都不支持 null，修复方式就是不要把 null 存进去。HashMap 的 Key 和 Value 可以存入 null，而 ConcurrentHashMap 看似是 HashMap 的线程安全版本，却不支持 null 值的 Key 和 Value，这是容易产生误区的一个地方。
//    对于类似 fooService.getBarService().bar().equals(“OK”) 的级联调用，需要判空的地方有很多，包括 fooService、getBarService() 方法的返回值，以及 bar 方法返回的字符串。如果使用 if-else 来判空的话可能需要好几行代码，但使用 Optional 的话一行代码就够了。
//    对于 rightMethod 返回的 List，由于不能确认其是否为 null，所以在调用 size 方法获得列表大小之前，同样可以使用 Optional.ofNullable 包装一下返回值，然后通过.orElse(Collections.emptyList()) 实现在 List 为 null 的时候获得一个空的 List，最后再调用 size 方法。
    private List<String> rightMethod(FoodService foodService, Integer i,String s, String t) {
        log.info("result{}{}{}{}", Optional.ofNullable(i).orElse(0) + 1,"OK".equals(s), Objects.equals(t,s),new HashMap<String,String>().put(null,null));
        Optional.ofNullable(foodService).map(FoodService::getBarService).filter(barService -> "OK".equals(barService.bar())).ifPresent(result->log.info("OK"));
        return new ArrayList<>();
    }
}


class FoodService{
    @Getter
    private BarService barService;
}

class BarService{
    String bar() {
        return "OK";
    }
}
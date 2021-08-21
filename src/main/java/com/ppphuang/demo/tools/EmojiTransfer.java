package com.ppphuang.demo.tools;

import com.vdurmont.emoji.EmojiParser;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmojiTransfer {

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String parseToAliases(String source) {
        if (source == null) {
            return "";
        }
//        String str1 = "An 😀awesome 😃string with a few 😉emojis!";
        return EmojiParser.parseToAliases(source);
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String transferEmoji(String source) {
        if (source == null) {
            return "";
        }
//        String str1 = "An 😀awesome 😃string with a few 😉emojis!";
        return EmojiParser.parseToAliases(source);
    }

    /**
     * 批量过滤emoji 或者 其他非文字类型的字符
     *
     * @param sources
     * @return
     */
    public static Map<String, String> multiTransferEmoji(List<String> sources) {
        if (sources == null || sources.isEmpty()) {
            return null;
        }
//        String str1 = "An 😀awesome 😃string with a few 😉emojis!";
        return sources.stream().collect(Collectors.toMap((source) -> source, EmojiParser::parseToAliases, (k1, k2) -> k2));
    }

    /**
     * 批量解码emoji 或者 其他非文字类型的字符
     *
     * @param sources
     * @return
     */
    public static Map<String, String> multiUnicodeEmoji(List<String> sources) {
        if (sources == null || sources.isEmpty()) {
            return null;
        }
//        String str1 = "An 😀awesome 😃string with a few 😉emojis!";
        return sources.stream().collect(Collectors.toMap((source) -> source, EmojiParser::parseToUnicode, (k1, k2) -> k2));
    }

    /**
     * 转换emoji
     *
     * @param source
     * @return
     */
    public static String parseToUnicode(String source) {
        if (source == null) {
            return "";
        }
//        String str = "An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!";
        return EmojiParser.parseToUnicode(source);
    }
}

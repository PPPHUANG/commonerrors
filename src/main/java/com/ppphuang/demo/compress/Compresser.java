package com.ppphuang.demo.compress;

public interface Compresser {
    byte[] compress(byte[] bytes);


    byte[] decompress(byte[] bytes);
}

package com.geekbang.springaop.customxml;

import java.util.List;

/**
 * Created by yandex on 2020/11/16.
 */
public class Klass {
    private List<Cat> cats;

    public void setCats(List<Cat> cats) {
        this.cats = cats;
    }

    public List<Cat> getCats() {
        return cats;
    }

    @Override
    public String toString() {
        return "Klass{" +
                "cats=" + cats +
                '}';
    }

    public void print() {
        System.out.println(this.getCats());
    }
}

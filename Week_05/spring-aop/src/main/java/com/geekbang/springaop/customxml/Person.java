package com.geekbang.springaop.customxml;

/**
 * Created by yandex on 2020/11/16.
 */
public class Person implements Animals{
    private Klass classAnimal;

    public void setClassAnimal(Klass classAnimal) {
        this.classAnimal = classAnimal;
    }

    public Klass getClassAnimal() {
        return classAnimal;
    }

    private Cat cat;

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public Cat getCat() {
        return cat;
    }

    @Override
    public void cry() {
        System.out.println("ClassAnimals Have " + this.classAnimal.getCats().size() + " cats and one is " + this.cat);
    }
}

// Person
package model;
import java.util.Objects;

public abstract class Person {
    protected String name;
    protected int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }

    public abstract void displayInfo();

    @Override
    public boolean equals(Object o){
        return o instanceof Person p &&
                age == p.age &&
                Objects.equals(name,p.name);}
    @Override
    public int hashCode(){
        return Objects.hash(name,age);}
}

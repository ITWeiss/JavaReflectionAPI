package ioc;

public class Main {
    public static void main(String[] args) {
        IoCContainer container = new IoCContainer();

        Engine e1 = container.getInstance(Engine.class);
        Engine e2 = container.getInstance(Engine.class);

        System.out.println(e1 == e2);

        Car c1 = container.getInstance(Car.class);
        Car c2 = container.getInstance(Car.class);

        System.out.println(c1 == c2);

        c1.drive();
    }
}

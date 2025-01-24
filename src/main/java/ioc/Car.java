package ioc;

@Component
@Scope()
public class Car {

    @Inject
    private Engine engine;

    void drive() {
        engine.start();
        System.out.println("Car is driving.");
    }
}

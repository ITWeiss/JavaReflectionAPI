package ioc;

@Component
@Scope()
public class Engine {

    @Value(value = "1596")
    private int engineDisplacement;

    public void start() {
        System.out.println("Engin started.");
        System.out.printf("Engine Displacement: %d.\n", engineDisplacement);
    }
}

package cp.Week9.CookingRace;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

class BenignDish {
    private final String name;
    private final String[] steps;
    private int currentStep = 0;

    public BenignDish(String name, String[] steps) {
        this.name = name;
        this.steps = steps;
    }

    //The increment is not atomic, but we don't care.
    public Optional<String> getNextStep() {
        int newlen = currentStep++;
        if (newlen < steps.length)
            return Optional.of(steps[newlen]);
        else
            return Optional.empty();    
    }

    public boolean isCompleted() {
        return currentStep >= steps.length;
    }

    public String getName() {
        return name;
    }
}

class BenignChef extends Thread {
    private final String name;
    private final List<BenignDish> dishes;
    private final Random random = new Random();

    public BenignChef(String name, List<BenignDish> dishes) {
        this.name = name;
        this.dishes = dishes;
    }

    public void run() {
        while (true) {
            // Randomly select a dish
            BenignDish chosenDish = dishes.get(random.nextInt(dishes.size()));

            // Try to get a step
            String step = chosenDish.getNextStep().orElse(null);
            if (step == null) {
                // Check if all dishes are done
                if (dishes.stream().allMatch(BenignDish::isCompleted)) {
                    System.out.println("🎉 " + name + " has finished all available tasks!");
                    break;
                }
                continue; // Try again with another dish
            }

            {
                System.out.println("🙃 " + name + " is working on " + chosenDish.getName() + " (" + step + ")...");
                try {
                    Thread.sleep(2000); // Simulate work time
                } catch (InterruptedException ignored) {}
                System.out.println("✅ " + name + " completed " + chosenDish.getName() + " (" + step + ").");
            }
        }
    }
}

public class CookingBenignRace {
    public static void main(String[] args) {

        List<BenignDish> dishes = new ArrayList<>();
        dishes.add(new BenignDish("Dish I", new String[]{"Chop tomatoes", "Chop onion", "Add olive oil"}));
        dishes.add(new BenignDish("Dish II", new String[]{"Cook pasta in pan", "Cook meat in the oven", "Add garnish"}));

        BenignChef chef1 = new BenignChef("Chef Alice", dishes);
        BenignChef chef2 = new BenignChef("Chef Bob", dishes);

        chef1.start();
        chef2.start();
    }
}
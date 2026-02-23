package cp.Week9.CookingRace;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

class FryingPan {
    private boolean isOccupied = false; // Shared mutable state

    public void use(String chefName, String dish, String step) {
        System.out.println("🍳 " + chefName + " is trying to use the pan for " + dish + " (" + step + ")...");
        
        if (isOccupied) {
            System.out.println("🔥 RACE CONDITION! " + chefName + " is using the pan while it's already occupied!");
        }

        isOccupied = true;
        //System.out.println(chefName + " is cooking " + dish + " (" + step + ")...");
        
        try { Thread.sleep(100); } catch (InterruptedException ignored) {} // Simulate cooking time
        
        System.out.println("✅ " + chefName + " finished cooking " + dish + " (" + step + ").");
        isOccupied = false;
    }
}

class Dish {
    private final String name;
    private final String[] steps;
    private int currentStep = 0;

    public Dish(String name, String[] steps) {
        this.name = name;
        this.steps = steps;
    }

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

class Chef extends Thread {
    private final String name;
    private final List<Dish> dishes;
    private final FryingPan pan;
    private final Random random = new Random();

    public Chef(String name, List<Dish> dishes, FryingPan pan) {
        this.name = name;
        this.dishes = dishes;
        this.pan = pan;
    }

    public void run() {
        int tasksCompleted = 0;
        while (true) {
            // Randomly select a dish
            Dish chosenDish = dishes.get(random.nextInt(dishes.size()));

            // Try to get a step
            String step = chosenDish.getNextStep().orElse(null);
            if (step == null) {
                // Check if all dishes are done
                if (dishes.stream().allMatch(Dish::isCompleted)) {
                    System.out.println("🎉 " + name + " has finished all available tasks! Tasks completed: " + tasksCompleted + ".");
                    break;
                }
                continue; // Try again with another dish
            }

            if (step.contains("pan")) {
                pan.use(name, chosenDish.getName(), step); // No synchronization here!
            } else {
                System.out.println("🙃 " + name + " is working on " + chosenDish.getName() + " (" + step + ")...");
                try {
                    Thread.sleep(2000); // Simulate work time
                } catch (InterruptedException ignored) {}
                System.out.println("✅ " + name + " completed " + chosenDish.getName() + " (" + step + ").");
            }
            tasksCompleted++;
        }
    }
}

public class CookingRaceCondition {
    public static void main(String[] args) {
        FryingPan sharedPan = new FryingPan();

        List<Dish> dishes = new ArrayList<>();
        
        dishes.add(new Dish("Dish I", new String[]{"Chop tomatoes", "Chop onion", "Boil Pasta"}));
        
        // The next dish is tricky!
        dishes.add(new Dish("Dish II", new String[]{"Cook veggies in pan", "Cook meat in pan", "Add garnish"}));

        Chef chef1 = new Chef("Chef Alice", dishes, sharedPan);
        Chef chef2 = new Chef("Chef Bob", dishes, sharedPan);

        chef1.start();
        chef2.start();
    }
}
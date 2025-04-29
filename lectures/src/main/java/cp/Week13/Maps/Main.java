package cp.Week13.Maps;

import cp.Utils;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting ConcurrentMap:");
        Utils.benchmark(ConcurrentMap::main);
        //System.out.println("Starting SpinlockMap2T:");
        //Utils.benchmark(SpinlockMap2T::main);
        System.out.println("Starting SynchronizedMap:");
        Utils.benchmark(SynchronizedMap::main);
        }       
}
    

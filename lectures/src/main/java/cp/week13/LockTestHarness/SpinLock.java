package cp.week13.LockTestHarness;

import java.util.concurrent.atomic.AtomicBoolean;

class SpinLock implements LockStrategy {
    private final AtomicBoolean locked = new AtomicBoolean(false);

    public void lock() {
        while (!locked.compareAndSet(false, true)) {
            Thread.onSpinWait();
        }
    }

    public void unlock() {
        locked.set(false);
    }
}

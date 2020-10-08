package nl.svenkonings.jacomo.solvers.ortools;

import com.skaggsm.ortools.OrToolsHelper;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Responsible for loading the native component of the OR-Tools library.
 */
public class OrToolsLoader {
    private static boolean LIBRARY_LOADED = false;
    private static final ReentrantLock LOCK = new ReentrantLock();

    /**
     * Load the native component of the OR-Tools library.
     */
    public static void loadLibrary() {
        if (!LIBRARY_LOADED) {
            LOCK.lock();
            try {
                if (!LIBRARY_LOADED) {
                    OrToolsHelper.loadLibrary();
                    LIBRARY_LOADED = true;
                }
            } finally {
                LOCK.unlock();
            }
        }
    }
}

package nl.svenkonings.jacomo.solvers.ortools;

import com.skaggsm.ortools.OrToolsHelper;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Responsible for loading the native component of the OR-Tools library.
 */
public class OrToolsLoader {
    private static final AtomicBoolean LIBRARY_LOADED = new AtomicBoolean();

    /**
     * Load the native component of the OR-Tools library.
     */
    public static void loadLibrary() {
        if (LIBRARY_LOADED.compareAndSet(false, true)) {
            OrToolsHelper.loadLibrary();
        }
    }
}

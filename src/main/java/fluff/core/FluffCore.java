package fluff.core;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fluff.core.internal.Dependency;
import fluff.core.internal.DependencyResolver;
import fluff.core.lib.IFluffLib;
import fluff.core.lib.IFluffLibSupplier;
import fluff.core.lib.LibraryMain;
import fluff.core.lib.v1.FluffLibV1Supplier;

/**
 * The core class for managing Fluff libraries.
 */
public class FluffCore {
	
    private static boolean INITIALIZED = false;
    
    /**
     * Initializes the FluffCore library. This method should be called at the start of the program
     * to ensure proper initialization of libraries using the Fluff API.
     * 
     * @throws Exception if an error occurs during initialization
     */
    @LibraryMain
    public static void init() throws Exception {
        if (INITIALIZED) return;
        
        registerSupplier(new FluffLibV1Supplier());
        
        ClassLoader loader = FluffCore.class.getClassLoader();
        
        List<URL> infos = new ArrayList<>();
        loader.getResources("fluff_lib.info")
                .asIterator()
                .forEachRemaining(url -> {
                    infos.add(url);
                });
        
        DependencyResolver.resolveAndLoad(loader, infos);
        
        INITIALIZED = true;
    }
    
    /**
     * Associates the specified supplier with its ID in the library info suppliers map.
     * 
     * @param supplier the supplier to be associated with the specified ID
     */
    public static void registerSupplier(IFluffLibSupplier supplier) {
        DependencyResolver.INFO_SUPPLIERS.put(supplier.getID(), supplier);
    }
    
    /**
     * Retrieves a list of loaded Fluff libraries.
     * 
     * @return a list of loaded Fluff libraries
     */
    public static Collection<IFluffLib> getLibs() {
        return DependencyResolver.LOADED.values().stream().map(Dependency::getLib).toList();
    }
    
    /**
     * Finds a Fluff library by its tag.
     * 
     * @param tag the tag of the library to find
     * @return the Fluff library with the specified tag, or null if not found
     */
    public static IFluffLib findLib(String tag) {
        return DependencyResolver.LOADED.get(tag).getLib();
    }
}

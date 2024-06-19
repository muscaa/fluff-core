package fluff.core;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        
        List<URL> infos = new ArrayList<>();
        FluffCore.class.getClassLoader()
                .getResources("fluff_lib.info")
                .asIterator()
                .forEachRemaining(url -> {
                    infos.add(url);
                });
        
        DependencyResolver.resolveAndLoad(infos);
        
        INITIALIZED = true;
    }
    
    /**
     * Associates the specified supplier with the specified ID in the library info suppliers map.
     * 
     * @param id the ID with which the specified supplier is to be associated
     * @param provider the supplier to be associated with the specified ID
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
    
    public static IFluffLib findLib(String tag) {
        return DependencyResolver.LOADED.get(tag).getLib();
    }
}

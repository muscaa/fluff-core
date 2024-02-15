package fluff.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fluff.core.lib.IFluffLib;
import fluff.core.lib.IFluffLibSupplier;
import fluff.core.lib.LibraryMain;
import fluff.core.lib.info.LibraryInfoReader;
import fluff.core.lib.v1.FluffLibV1;

/**
 * The core class for managing Fluff libraries.
 */
public class FluffCore {
	
    private static boolean INITIALIZED = false;
    private static final Map<String, IFluffLibSupplier> LIBINFO_SUPPLIERS = new HashMap<>();
    private static final List<IFluffLib> LIBS = new ArrayList<>();
    
    /**
     * Initializes the FluffCore library. This method should be called at the start of the program
     * to ensure proper initialization of libraries using the FluffCore API.
     * 
     * @throws Exception if an error occurs during initialization
     */
    @LibraryMain
    public static void init() throws Exception {
        if (INITIALIZED) return;
        INITIALIZED = true;
        
        putLibraryInfoSupplier("v1", FluffLibV1::new);
        
        List<URL> remaining = new ArrayList<>();
        ClassLoader.getSystemClassLoader()
                .getResources("fluff_lib.info")
                .asIterator()
                .forEachRemaining(url -> {
                    remaining.add(url);
                });
        
        loadLibs(remaining);
    }
    
    private static void loadLibs(List<URL> remaining) {
        int i = 0;
        while (!remaining.isEmpty()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(remaining.get(i).openStream()))) {
                String version = br.readLine();
                IFluffLibSupplier supplier = LIBINFO_SUPPLIERS.get(version);
                
                if (supplier != null) {
                    IFluffLib lib = supplier.loadLibrary(new LibraryInfoReader(br));
                    
                    LIBS.add(lib);
                    remaining.remove(i);
                    
                    System.out.println("loaded " + lib.getID());
                } else {
                    i++;
                    if (i >= remaining.size()) i = 0;
                }
            } catch (Exception e) {}
        }
    }
    
    /**
     * Associates the specified supplier with the specified ID in the library info suppliers map.
     * 
     * @param id the ID with which the specified supplier is to be associated
     * @param provider the supplier to be associated with the specified ID
     */
    public static void putLibraryInfoSupplier(String id, IFluffLibSupplier provider) {
        LIBINFO_SUPPLIERS.put(id, provider);
    }

    
    /**
     * Retrieves a list of loaded Fluff libraries.
     * 
     * @return a list of loaded Fluff libraries
     */
    public static List<IFluffLib> getLibs() {
        return LIBS;
    }
    
    /**
     * Finds a Fluff library by author and ID.
     * 
     * @param author the author of the library
     * @param id the ID of the library
     * @return the Fluff library if found, or null otherwise
     */
    public static IFluffLib findLib(String author, String id) {
        for (IFluffLib lib : LIBS) {
            if (lib.getAuthor().equals(author) && lib.getID().equals(id)) return lib;
        }
        return null;
    }
}

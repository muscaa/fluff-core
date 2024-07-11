package fluff.core;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fluff.core.lib.ILibrary;
import fluff.core.lib.ILibraryManager;
import fluff.core.lib.LibraryResolver;
import fluff.core.lib.v1.V1LibraryManager;
import fluff.core.lib.v1.LibraryMain;

public class FluffCore {
	
    private static boolean INITIALIZED = false;
    
    @LibraryMain
    public static void init() throws Exception {
        if (INITIALIZED) return;
        
        register(new V1LibraryManager());
        
        ClassLoader loader = FluffCore.class.getClassLoader();
        
        List<URL> infos = new ArrayList<>();
        loader.getResources("fluff_lib.info")
                .asIterator()
                .forEachRemaining(url -> {
                    infos.add(url);
                });
        
        LibraryResolver.resolveAndLoad(loader, infos);
        
        INITIALIZED = true;
    }
    
    public static void register(ILibraryManager manager) {
        LibraryResolver.MANAGERS.put(manager.getID(), manager);
    }
    
    public static Collection<ILibrary> getLibs() {
        return LibraryResolver.LOADED.values().stream().map(r -> r.library).toList();
    }
    
    public static ILibrary findLib(String tag) {
        return LibraryResolver.LOADED.get(tag).library;
    }
}

package fluff.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import fluff.core.lib.IFluffLib;
import fluff.core.lib.IFluffLibSupplier;
import fluff.core.lib.LibraryException;
import fluff.core.lib.info.LibraryInfoReader;

class DependencyResolver {
	
    public static final Map<String, IFluffLibSupplier> INFO_SUPPLIERS = new HashMap<>();
    public static final Map<String, Dependency> LOADED = new HashMap<>();
    
    public static void resolveAndLoad(ClassLoader loader, List<URL> infos) throws LibraryException {
    	List<URL> unresolved = new ArrayList<>(infos);
    	
    	while (!unresolved.isEmpty()) {
    		Map<String, Dependency> resolved = resolve(unresolved);
    		
    		load(loader, resolved);
    	}
    }
    
    private static void load(ClassLoader loader, Map<String, Dependency> resolved) throws LibraryException {
    	Queue<Dependency> queue = new LinkedList<>();
    	
    	for (Map.Entry<String, Dependency> e : resolved.entrySet()) {
    		Dependency dep = e.getValue();
    		
    		if (dep.dependencyCount == 0) queue.add(dep);
    	}
    	
    	List<Dependency> sorted = new ArrayList<>();
    	
    	while (!queue.isEmpty()) {
    		Dependency current = queue.poll();
    		sorted.add(current);
    		
    		for (Dependency dnt : current.getDependents()) {
    			if (--dnt.dependencyCount == 0) queue.add(dnt);
    		}
    	}
    	
    	if (sorted.size() != resolved.size()) {
            Set<String> visited = new HashSet<>();
            Set<String> recursionStack = new HashSet<>();
            
        	for (Map.Entry<String, Dependency> e : resolved.entrySet()) {
        		Dependency dep = e.getValue();
        		
        		if (detectCycle(dep, visited, recursionStack)) {
                    throw new LibraryException("Cyclic dependency detected: " + recursionStack);
                }
        	}
    	}
    	
    	for (Dependency d : sorted) {
    		d.load(loader);
    		
    		LOADED.put(d.getTag(), d);
    	}
    }
    
    private static Map<String, Dependency> resolve(List<URL> unresolved) throws LibraryException {
    	Map<String, Dependency> resolved = new HashMap<>();
    	
    	for (int i = unresolved.size() - 1; i >= 0; i--) {
    		URL info = unresolved.get(i);
    		
    		try (BufferedReader br = new BufferedReader(new InputStreamReader(info.openStream()))) {
    			String supplierID = br.readLine();
    			IFluffLibSupplier supplier = INFO_SUPPLIERS.get(supplierID);
    			
    			if (supplier == null) {
    				br.close();
    				continue;
    			}
    			
				IFluffLib lib = supplier.createLibrary(new LibraryInfoReader(br));
				Dependency dep = new Dependency(supplier, lib);
				
				if (LOADED.containsKey(dep.getTag())) {
					throw new LibraryException("Library already loaded: " + dep.getTag());
				}
				if (resolved.containsKey(dep.getTag())) {
					throw new LibraryException("Overlapping tag: " + dep.getTag());
				}
				
				resolved.put(dep.getTag(), dep);
				unresolved.remove(i);
            } catch (IOException e) {
            	throw new LibraryException(e);
            }
    	}
    	
    	if (resolved.isEmpty()) throw new LibraryException("Couldn't resolve dependencies!");
    	
    	for (Map.Entry<String, Dependency> e : resolved.entrySet()) {
    		Dependency dep = e.getValue();
    		
    		for (String tag : dep.getLib().getDependencies()) {
    			if (LOADED.containsKey(tag)) {
    				dep.link(LOADED.get(tag));
    			} else if (resolved.containsKey(tag)) {
    				dep.link(resolved.get(tag));
    			} else {
    				throw new LibraryException("Missing dependency: " + tag);
    			}
    		}
    	}
    	
    	return resolved;
    }
    
    private static boolean detectCycle(Dependency dep, Set<String> visited, Set<String> recursionStack) {
    	if (dep.isLoaded()) return false;
        if (recursionStack.contains(dep.getTag())) return true;
        if (visited.contains(dep.getTag())) return false;
        
        visited.add(dep.getTag());
        recursionStack.add(dep.getTag());
        
        for (Dependency dependent : dep.getDependents()) {
            if (detectCycle(dependent, visited, recursionStack)) return true;
        }
        
        recursionStack.remove(dep.getTag());
        
        return false;
    }
}

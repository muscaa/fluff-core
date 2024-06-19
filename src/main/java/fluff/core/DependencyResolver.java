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
    
    public static void resolveAndLoad(List<URL> infos) throws LibraryException {
    	List<URL> infosCopy = new ArrayList<>(infos);
    	
    	while (!infosCopy.isEmpty()) {
    		Map<String, Dependency> resolved = resolveAndRemove(infosCopy);
    		
    		load(resolved);
    	}
    }
    
    private static void load(Map<String, Dependency> resolved) throws LibraryException {
    	Map<String, Integer> dependenciesMap = new HashMap<>();
    	Queue<Dependency> queue = new LinkedList<>();
    	
    	for (Map.Entry<String, Dependency> e : resolved.entrySet()) {
    		Dependency d = e.getValue();
    		int dependencies = d.getDependencies().size();
    		
    		dependenciesMap.put(d.getTag(), dependencies);
    		
    		if (dependencies == 0) queue.add(d);
    	}
    	
    	List<Dependency> sorted = new ArrayList<>();
    	
    	while (!queue.isEmpty()) {
    		Dependency current = queue.poll();
    		sorted.add(current);
    		
    		for (Dependency dependent : current.getDependents()) {
    			int dependencies = dependenciesMap.get(dependent.getTag()) - 1;
    			dependenciesMap.put(dependent.getTag(), dependencies);
    			
    			if (dependencies == 0) queue.add(dependent);
    		}
    	}
    	
    	if (sorted.size() != resolved.size()) {
            Set<String> visited = new HashSet<>();
            Set<String> recursionStack = new HashSet<>();
            
        	for (Map.Entry<String, Dependency> e : resolved.entrySet()) {
        		Dependency d = e.getValue();
        		
        		if (detectCycle(d, visited, recursionStack)) {
                    throw new LibraryException("Cyclic dependency detected: " + recursionStack);
                }
        	}
    	}
    	
    	for (Dependency d : sorted) {
    		d.load();
    		
    		LOADED.put(d.getTag(), d);
    	}
    }
    
    private static Map<String, Dependency> resolveAndRemove(List<URL> infos) throws LibraryException {
    	Map<String, Dependency> resolved = new HashMap<>();
    	
    	for (int i = infos.size() - 1; i >= 0; i--) {
    		URL info = infos.get(i);
    		
    		try (BufferedReader br = new BufferedReader(new InputStreamReader(info.openStream()))) {
    			String supplierID = br.readLine();
    			IFluffLibSupplier supplier = INFO_SUPPLIERS.get(supplierID);
    			
    			if (supplier == null) {
    				br.close();
    				continue;
    			}
    			
				IFluffLib lib = supplier.createLibrary(new LibraryInfoReader(br));
				Dependency d = new Dependency(supplier, lib);
				
				if (LOADED.containsKey(d.getTag())) throw new LibraryException("Library already loaded: " + d.getTag());
				if (resolved.containsKey(d.getTag())) throw new LibraryException("Overlapping tag: " + d.getTag());
				
				resolved.put(d.getTag(), d);
				infos.remove(i);
            } catch (IOException e) {
            	throw new LibraryException(e);
            }
    	}
    	
    	if (resolved.isEmpty()) throw new LibraryException("Couldn't resolve dependencies!");
    	
    	for (Map.Entry<String, Dependency> e : resolved.entrySet()) {
    		Dependency d = e.getValue();
    		
    		for (String tag : d.getLib().getDependencies()) {
    			if (LOADED.containsKey(tag)) continue;
    			if (!resolved.containsKey(tag)) throw new LibraryException("Missing dependency: " + tag);
    			
    			d.link(resolved.get(tag));
    		}
    	}
    	
    	return resolved;
    }
    
    private static boolean detectCycle(Dependency d, Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(d.getTag())) {
            return true;
        }
        
        if (visited.contains(d.getTag())) {
            return false;
        }
        
        visited.add(d.getTag());
        recursionStack.add(d.getTag());
        
        for (Dependency dependent : d.getDependents()) {
            if (detectCycle(dependent, visited, recursionStack)) {
                return true;
            }
        }
        
        recursionStack.remove(d.getTag());
        return false;
    }
}

package fluff.core;

import java.util.LinkedList;
import java.util.List;

import fluff.core.lib.IFluffLib;
import fluff.core.lib.IFluffLibSupplier;
import fluff.core.lib.ILoadableFluffLib;
import fluff.core.lib.LibraryException;

class Dependency {
	
	private final List<Dependency> dependents = new LinkedList<>();
	private final List<Dependency> dependencies = new LinkedList<>();
	private final IFluffLibSupplier supplier;
	private final IFluffLib lib;
	private final String tag;
	
	private boolean loaded = false;
	
	int dependencyCount = 0;
	
	public Dependency(IFluffLibSupplier supplier, IFluffLib lib) {
		this.supplier = supplier;
		this.lib = lib;
		this.tag = supplier.getTag(lib);
	}
	
	public void load(ClassLoader loader) throws LibraryException {
		if (loaded) return;
		
		if (lib instanceof ILoadableFluffLib loadable) {
			loadable.load(loader);
		}
		
		loaded = true;
	}
	
	public void link(Dependency dep) {
		dependencies.add(dep);
		dep.dependents.add(this);
		
		if (!dep.isLoaded()) dependencyCount++;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public List<Dependency> getDependents() {
		return dependents;
	}
	
	public List<Dependency> getDependencies() {
		return dependencies;
	}
	
	public IFluffLibSupplier getSupplier() {
		return supplier;
	}
	
	public IFluffLib getLib() {
		return lib;
	}
	
	public String getTag() {
		return tag;
	}
}

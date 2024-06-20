package fluff.core;

import java.util.LinkedList;
import java.util.List;

import fluff.core.lib.IFluffLib;
import fluff.core.lib.IFluffLibSupplier;
import fluff.core.lib.ILoadableFluffLib;
import fluff.core.lib.LibraryException;

/**
 * Represents a dependency in the Fluff library system.
 * Manages the loading and linking of dependencies and dependents.
 */
class Dependency {
	
	private final List<Dependency> dependents = new LinkedList<>();
	private final List<Dependency> dependencies = new LinkedList<>();
	private final IFluffLibSupplier supplier;
	private final IFluffLib lib;
	private final String tag;
	
	private boolean loaded = false;
	
	int dependencyCount = 0;
	
	/**
	 * Constructs a new Dependency with the specified supplier and library.
	 *
	 * @param supplier the supplier of the library
	 * @param lib the library instance
	 */
	public Dependency(IFluffLibSupplier supplier, IFluffLib lib) {
		this.supplier = supplier;
		this.lib = lib;
		this.tag = supplier.getTag(lib);
	}
	
	/**
	 * Loads the library using the specified class loader.
	 *
	 * @param loader the class loader to use for loading the library
	 * @throws LibraryException if an error occurs during loading
	 */
	public void load(ClassLoader loader) throws LibraryException {
		if (loaded) return;
		
		if (lib instanceof ILoadableFluffLib loadable) {
			loadable.load(loader);
		}
		
		loaded = true;
	}
	
	/**
	 * Links this dependency to another dependency.
	 * Establishes a dependency relationship between this dependency and the specified dependency.
	 *
	 * @param dep the dependency to link to
	 */
	public void link(Dependency dep) {
		dependencies.add(dep);
		dep.dependents.add(this);
		
		if (!dep.isLoaded()) dependencyCount++;
	}
	
	/**
	 * Checks if the library has been loaded.
	 *
	 * @return true if the library is loaded, false otherwise
	 */
	public boolean isLoaded() {
		return loaded;
	}
	
	/**
	 * Retrieves the list of dependents of this dependency.
	 *
	 * @return a list of dependents
	 */
	public List<Dependency> getDependents() {
		return dependents;
	}
	
	/**
	 * Retrieves the list of dependencies of this dependency.
	 *
	 * @return a list of dependencies
	 */
	public List<Dependency> getDependencies() {
		return dependencies;
	}
	
	/**
	 * Retrieves the supplier of the library.
	 *
	 * @return the supplier
	 */
	public IFluffLibSupplier getSupplier() {
		return supplier;
	}
	
	/**
	 * Retrieves the library instance.
	 *
	 * @return the library instance
	 */
	public IFluffLib getLib() {
		return lib;
	}
	
	/**
	 * Retrieves the tag associated with the library.
	 *
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
}

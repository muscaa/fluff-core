package fluff.core.lib;

import java.util.LinkedList;
import java.util.List;

public class ResolvedLibrary {
	
	public final List<ResolvedLibrary> dependents = new LinkedList<>();
	public final List<ResolvedLibrary> dependencies = new LinkedList<>();
	public final ILibraryManager manager;
	public final ILibrary library;
	public final String tag;
	
	public ResolvedLibrary(ILibraryManager manager, ILibrary library) {
		this.manager = manager;
		this.library = library;
		this.tag = manager.getTag(library);
	}
	
	public void link(ResolvedLibrary dep) {
		dependencies.add(dep);
		dep.dependents.add(this);
	}
	
	@Override
	public String toString() {
		return tag;
	}
	
	@Override
	public int hashCode() {
		return tag.hashCode();
	}
}

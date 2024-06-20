package fluff.core.lib.v1;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import fluff.core.lib.IFluffLib;
import fluff.core.lib.ILoadableFluffLib;
import fluff.core.lib.LibraryException;
import fluff.core.lib.LibraryMain;
import fluff.core.lib.info.LibraryInfoReader;

/**
 * Implementation of the {@link IFluffLib} interface for version 1 library info supplier.
 */
public class FluffLibV1 implements ILoadableFluffLib {
    
    private final String author;
    private final String id;
    private final Set<String> dependencies;
    private final String url;
    private final String libClassName;
    
    private Class<?> libClass;
    
    /**
     * Constructs a new FluffLibV1 instance using the provided LibraryInfoReader.
     * 
     * @param r the LibraryInfoReader containing the information required to initialize the library
     * @throws LibraryException if an error occurs during initialization
     */
    public FluffLibV1(LibraryInfoReader r) throws LibraryException {
        author = r.required("author", "Property missing: author")
                	.String();
        
        id = r.required("id", "Property missing: id")
                	.String();
        
        dependencies = r.optional("depends")
	                .transform(Set.class)
	                .If(Objects::nonNull, v -> {
	                    Set<String> set = new HashSet<>();
	                    for (String tag : v.split(",")) {
	                    	tag = tag.trim();
	                        set.add(tag);
	                    }
	                    return set;
	                })
	                .Else(v -> new HashSet<>())
	                .Result();
        if (!author.equals("muscaa") || !id.equals("fluff-core")) dependencies.add("muscaa/fluff-core");
        
        url = r.optional("url")
        			.String();
        
        libClassName = r.optional("class")
	                .String();
    }
    
    @Override
    public void load(ClassLoader loader) throws LibraryException {
    	if (libClassName == null) return;
    	
    	try {
			libClass = loader.loadClass(libClassName);
		} catch (ClassNotFoundException e) {
			throw new LibraryException(e);
		}
    	
    	for (Method m : libClass.getDeclaredMethods()) {
            if (!m.isAnnotationPresent(LibraryMain.class)) continue;
            if (!Modifier.isStatic(m.getModifiers())) continue;
            
            try {
                m.setAccessible(true);
                m.invoke(null);
            } catch (Exception e) {
                throw new LibraryException(e);
            }
        }
    }
    
    @Override
    public String getAuthor() {
        return author;
    }
    
    @Override
    public String getID() {
        return id;
    }
    
    @Override
    public Set<String> getDependencies() {
        return Set.copyOf(dependencies);
    }
    
    @Override
    public String getURL() {
        return url;
    }
    
    @Override
    public Class<?> getLibClass() {
        return libClass;
    }
}

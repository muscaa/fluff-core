package fluff.core.lib.v1;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private final List<String> dependencies;
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
	                .transform(List.class)
	                .If(Objects::nonNull, v -> {
	                    List<String> list = new ArrayList<>();
	                    for (String tag : v.split(",")) {
	                    	tag = tag.trim();
	                        list.add(tag);
	                    }
	                    return list;
	                })
	                .Else(v -> new ArrayList<>())
	                .Result();
        url = r.optional("url")
        			.String();
        libClassName = r.optional("class")
	                .String();
    }
    
    /**
     * Loads the library class and invokes methods annotated with {@link LibraryMain}.
     * 
     * @param loader the ClassLoader to use for loading the library class
     * @throws LibraryException if an error occurs while loading the library class or invoking methods
     */
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
    
    /**
     * Retrieves the author of the library.
     * 
     * @return the author of the library
     */
    @Override
    public String getAuthor() {
        return author;
    }
    
    /**
     * Retrieves the ID of the library.
     * 
     * @return the ID of the library
     */
    @Override
    public String getID() {
        return id;
    }
    
    /**
     * Retrieves the dependencies of the library.
     * 
     * @return a list of dependencies of the library
     */
    @Override
    public List<String> getDependencies() {
        return List.copyOf(dependencies);
    }
    
    /**
     * Retrieves the URL of the library, or null if it is not available.
     * 
     * @return the URL of the library, or null if not available
     */
    @Override
    public String getURL() {
        return url;
    }
    
    /**
     * Retrieves the class representing the library, or null if it is not available.
     * 
     * @return the class representing the library, or null if not available
     */
    @Override
    public Class<?> getLibClass() {
        return libClass;
    }
}

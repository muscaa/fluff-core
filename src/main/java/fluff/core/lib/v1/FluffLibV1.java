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
    private final Class<?> libClass;
    
    /**
     * Constructs a new FluffLibV1 instance using the provided LibraryInfoReader.
     * 
     * @param r the LibraryInfoReader containing the information required to initialize the library
     * @throws Exception if an error occurs during initialization
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
        libClass = r.optional("class")
	                .transform(Class.class)
	                .If(Objects::nonNull, v -> this.getClass().getClassLoader().loadClass(v))
	                .Result();
    }
    
    @Override
    public void load() throws LibraryException {
    	if (libClass == null) return;
    	
    	for (Method m : libClass.getDeclaredMethods()) {
            if (!m.isAnnotationPresent(LibraryMain.class)) continue;
            if (!Modifier.isStatic(m.getModifiers())) continue;
            
            //LibraryMain a = m.getAnnotation(LibraryMain.class);
            
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
    public List<String> getDependencies() {
        return List.copyOf(dependencies);
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

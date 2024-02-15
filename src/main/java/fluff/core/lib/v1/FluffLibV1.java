package fluff.core.lib.v1;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fluff.core.FluffCore;
import fluff.core.lib.IFluffLib;
import fluff.core.lib.LibraryException;
import fluff.core.lib.LibraryMain;
import fluff.core.lib.info.LibraryInfoReader;

/**
 * Implementation of the {@link IFluffLib} interface for version 1 library info supplier.
 */
public class FluffLibV1 implements IFluffLib {
    
    private final String author;
    private final String id;
    private final List<IFluffLib> dependencies;
    private final String url;
    private final Class<?> libClass;
    
    /**
     * Constructs a new FluffLibV1 instance using the provided LibraryInfoReader.
     * 
     * @param r the LibraryInfoReader containing the information required to initialize the library
     * @throws Exception if an error occurs during initialization
     */
    public FluffLibV1(LibraryInfoReader r) throws Exception {
        author = r.required("author", "author property missing!")
                	.String();
        id = r.required("id", "id property missing!")
                	.String();
        dependencies = r.optional("depends")
	                .transform(List.class)
	                .If(Objects::nonNull, v -> {
	                    List<IFluffLib> list = new ArrayList<>();
	                    for (String d : v.split(",")) {
	                        String[] split = d.split("/");
	                        if (split.length != 2) throw new LibraryException(d + " is not a valid dependency!");
	                        
	                        if (split[0].equals(author) && split[1].equals(id)) continue;
	                        
	                        IFluffLib dep = FluffCore.findLib(split[0], split[1]);
	                        if (dep == null) throw new LibraryException("Dependency not loaded!");
	                        
	                        list.add(dep);
	                    }
	                    return list;
	                })
	                .Else(v -> new ArrayList<>())
	                .Result();
        url = r.optional("url")
        			.String();
        libClass = r.optional("class")
	                .transform(Class.class)
	                .If(Objects::nonNull, v -> {
	                    Class<?> clazz = this.getClass().getClassLoader().loadClass(v);
	                    for (Method m : clazz.getDeclaredMethods()) {
	                        if (!m.isAnnotationPresent(LibraryMain.class)) continue;
	                        if (!Modifier.isStatic(m.getModifiers())) continue;
	                        
	                        //LibraryMain a = m.getAnnotation(LibraryMain.class);
	                        
	                        try {
	                            m.setAccessible(true);
	                            m.invoke(null);
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                    }
	                    return clazz;
	                })
	                .Result();
    }
    
    @Override
    public String getSupplierID() {
        return "v1";
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
    public List<IFluffLib> getDependencies() {
        return dependencies;
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

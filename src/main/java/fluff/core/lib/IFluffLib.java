package fluff.core.lib;

import java.util.List;

/**
 * Interface representing a Fluff library.
 */
public interface IFluffLib {
	
    /**
     * Retrieves the author of the library.
     * 
     * @return the author of the library
     */
    String getAuthor();
    
    /**
     * Retrieves the ID of the library.
     * 
     * @return the ID of the library
     */
    String getID();
    
    /**
     * Retrieves the dependencies of the library, which may be an empty list.
     * 
     * @return a list of dependencies of the library
     */
    List<String> getDependencies();
    
    /**
     * Retrieves the URL of the library, or null if it is not available.
     * 
     * @return the URL of the library, or null if not available
     */
    String getURL();
    
    /**
     * Retrieves the class representing the library, or null if it is not available.
     * 
     * @return the class representing the library, or null if not available
     */
    Class<?> getLibClass();
}

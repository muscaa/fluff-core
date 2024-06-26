package fluff.core.lib;

import java.io.BufferedReader;

/**
 * Interface for supplying Fluff libraries.
 * 
 * @param <V> the type of Fluff library
 */
public interface IFluffLibSupplier<V extends IFluffLib> {
    
    /**
     * Creates a Fluff library using the provided BufferedReader.
     * 
     * @param reader the BufferedReader used for reading properties and creating the library
     * @return the created Fluff library
     * @throws LibraryException if an error occurs while creating the library
     */
    V createLibrary(BufferedReader reader) throws LibraryException;
    
    /**
     * Retrieves the unique tag associated with the given library.
     * 
     * @param lib the library for which the tag is to be retrieved
     * @return the tag of the library
     */
    String getTag(V lib);
    
    /**
     * Retrieves the unique ID of this library supplier.
     * 
     * @return the ID of this library supplier
     */
    String getID();
}

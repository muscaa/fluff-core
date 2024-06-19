package fluff.core.lib;

import fluff.core.lib.info.LibraryInfoReader;

/**
 * Interface for supplying Fluff libraries.
 */
public interface IFluffLibSupplier<V extends IFluffLib> {
    
    /**
     * Creates a Fluff library using the provided LibraryInfoReader.
     * 
     * @param reader the LibraryInfoReader to use for creating the library
     * @return the created Fluff library
     * @throws Exception if an error occurs while creating the library
     */
    V createLibrary(LibraryInfoReader reader) throws LibraryException;
    
    String getTag(V lib);
    
    String getID();
}

package fluff.core.lib;

import fluff.core.lib.info.LibraryInfoReader;

/**
 * Interface for supplying Fluff libraries.
 */
public interface IFluffLibSupplier {
    
    /**
     * Loads a Fluff library using the provided LibraryInfoReader.
     * 
     * @param reader the LibraryInfoReader to use for loading the library
     * @return the loaded Fluff library
     * @throws Exception if an error occurs while loading the library
     */
    IFluffLib loadLibrary(LibraryInfoReader reader) throws Exception;
}

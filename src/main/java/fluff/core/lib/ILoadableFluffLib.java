package fluff.core.lib;

/**
 * Represents a Fluff library that can be loaded with a specified ClassLoader.
 * Extends {@link IFluffLib}.
 */
public interface ILoadableFluffLib extends IFluffLib {
    
    /**
     * Loads the library using the specified ClassLoader.
     * 
     * @param loader the ClassLoader to use for loading the library
     * @throws LibraryException if an error occurs while loading the library
     */
    void load(ClassLoader loader) throws LibraryException;
}

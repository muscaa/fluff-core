package fluff.core.lib.v1;

import java.io.BufferedReader;

import fluff.core.lib.IFluffLibSupplier;
import fluff.core.lib.LibraryException;
import fluff.core.lib.info.LibraryInfoReader;

/**
 * Supplier for FluffLibV1 libraries.
 */
public class FluffLibV1Supplier implements IFluffLibSupplier<FluffLibV1> {
	
    public static final String ID = "v1";
	
    @Override
    public FluffLibV1 createLibrary(BufferedReader reader) throws LibraryException {
        return new FluffLibV1(new LibraryInfoReader(reader));
    }
	
    @Override
    public String getTag(FluffLibV1 lib) {
        return lib.getAuthor() + "/" + lib.getID();
    }
	
    @Override
    public String getID() {
        return ID;
    }
}

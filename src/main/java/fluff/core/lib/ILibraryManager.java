package fluff.core.lib;

import java.io.BufferedReader;
import java.io.IOException;

public interface ILibraryManager<V extends ILibrary> {
    
	V create(BufferedReader reader) throws LibraryException, IOException;
	
	void load(ClassLoader loader, V library) throws LibraryException;
    
    String getTag(V library);
    
    String getID();
}

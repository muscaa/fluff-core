package fluff.core.lib;

import java.util.Set;

public interface ILibrary {
    
    String getAuthor();
    
    String getID();
    
    Set<String> getDependencies();
    
    String getURL();
    
    String getMainClass();
}

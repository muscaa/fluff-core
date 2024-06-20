package fluff.core.lib.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.HashMap;
import java.util.Map;

import fluff.core.lib.LibraryException;

/**
 * Utility class for reading library information from a BufferedReader.
 */
public class LibraryInfoReader {
    
    protected final Map<String, LibraryInfoValue> properties = new HashMap<>();
    
    /**
     * Constructs a new LibraryInfoReader and populates the properties map with information read from the BufferedReader.
     * 
     * @param br the BufferedReader containing the library information
     * @throws IOException if an error occurs while reading the information
     * @throws UnexpectedException if the line is not a valid property
     */
    public LibraryInfoReader(BufferedReader br) throws IOException, UnexpectedException {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            
            if (line.isEmpty()) continue;
            if (line.startsWith("#")) continue;
            
            int comment = line.indexOf('#');
            if (comment != -1) {
                line = line.substring(0, comment).trim();
            }
            
            int space = line.indexOf(' ');
            if (space == -1) throw new UnexpectedException("\"" + line + "\" is not a valid property!");
            
            String key = line.substring(0, space);
            String value = line.substring(space + 1).trim();
            
            properties.put(key, new LibraryInfoValue(value));
        }
    }
    
    /**
     * Retrieves the value of the specified optional property.
     * 
     * @param key the key of the property
     * @return the value of the property if present, or null otherwise
     */
    public LibraryInfoValue optional(String key) {
        return optional(key, null);
    }
    
    /**
     * Retrieves the value of the specified optional property, using the specified default value if the property is not present.
     * 
     * @param key the key of the property
     * @param defaultValue the default value to return if the property is not present
     * @return the value of the property if present, or the default value otherwise
     */
    public LibraryInfoValue optional(String key, String defaultValue) {
        return properties.containsKey(key) ? properties.get(key) : new LibraryInfoValue(defaultValue);
    }
    
    /**
     * Retrieves the value of the specified required property.
     * 
     * @param key the key of the property
     * @param exception the exception to throw if the property is not present
     * @return the value of the property if present
     * @throws LibraryException if the property is not present
     */
    public LibraryInfoValue required(String key, String exception) throws LibraryException {
        return required(key, new LibraryException(exception));
    }
    
    /**
     * Retrieves the value of the specified required property.
     * 
     * @param key the key of the property
     * @param exception the exception to throw if the property is not present
     * @return the value of the property if present
     * @throws LibraryException if the property is not present
     */
    public LibraryInfoValue required(String key, LibraryException exception) throws LibraryException {
        if (!properties.containsKey(key)) throw exception;
        return properties.get(key);
    }
}

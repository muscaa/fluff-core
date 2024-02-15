package fluff.core.lib.info;

/**
 * Represents a value parsed from library information.
 */
public class LibraryInfoValue {
    
    private final String value;
    
    /**
     * Constructs a new LibraryInfoValue with the specified value.
     *
     * @param value the value to be represented
     */
    public LibraryInfoValue(String value) {
        this.value = value;
    }
    
    /**
     * Transforms the value to the specified type.
     *
     * @param clazz the class representing the desired type
     * @param <V> the type to transform to
     * @return a LibraryInfoTransform instance with the transformed value
     */
    public <V> LibraryInfoTransform<String, V> transform(Class<V> clazz) {
        return new LibraryInfoTransform<>(null, value);
    }
    
    /**
     * Retrieves the value as a string.
     *
     * @return the value as a string
     */
    public String String() {
        return value;
    }
    
    /**
     * Retrieves the value as a boolean.
     *
     * @return the value as a boolean
     * @throws Exception if the value cannot be parsed as a boolean
     */
    public boolean Boolean() throws Exception {
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Retrieves the value as an integer.
     *
     * @return the value as an integer
     * @throws Exception if the value cannot be parsed as an integer
     */
    public int Int() throws Exception {
        return Integer.parseInt(value);
    }
    
    /**
     * Retrieves the value as a float.
     *
     * @return the value as a float
     * @throws Exception if the value cannot be parsed as a float
     */
    public float Float() throws Exception {
        return Float.parseFloat(value);
    }
    
    /**
     * Retrieves the value as a long.
     *
     * @return the value as a long
     * @throws Exception if the value cannot be parsed as a long
     */
    public long Long() throws Exception {
        return Long.parseLong(value);
    }
    
    /**
     * Retrieves the value as a double.
     *
     * @return the value as a double
     * @throws Exception if the value cannot be parsed as a double
     */
    public double Double() throws Exception {
        return Double.parseDouble(value);
    }
    
    /**
     * Checks if the value is null.
     *
     * @return true if the value is null, false otherwise
     */
    public boolean isNull() {
        return value == null;
    }
    
    /**
     * Checks if the value is not null.
     *
     * @return true if the value is not null, false otherwise
     */
    public boolean isNotNull() {
        return value != null;
    }
}

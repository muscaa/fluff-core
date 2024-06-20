package fluff.core.lib;

public interface ILoadableFluffLib extends IFluffLib {
	
	void load(ClassLoader loader) throws LibraryException;
}

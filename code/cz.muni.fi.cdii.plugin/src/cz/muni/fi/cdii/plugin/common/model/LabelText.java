package cz.muni.fi.cdii.plugin.common.model;

/**
 * Graph elements implement this interface to provide label for their graph representation.
 *
 */
public interface LabelText {
	
	/**
	 * @return Text to by shown as graph element label.
	 */
	public String toLabelString();
}

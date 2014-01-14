package cz.muni.fi.cdii.plugin.visual;

/**
 * This exception is thrown if something goes wrong during CDI inspection of local projects.
 *
 */
public class LocalInspectionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LocalInspectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public LocalInspectionException(String message) {
		super(message);
	}

}

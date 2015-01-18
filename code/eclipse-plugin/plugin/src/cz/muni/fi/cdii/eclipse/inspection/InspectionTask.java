package cz.muni.fi.cdii.eclipse.inspection;


/**
 * Class defining a procedure leading to show/update CDI graph.
 *
 */
public interface InspectionTask {

	/**
	 * It invokes (re)inspection of CDI bean relations.
	 */
    public void run();
}

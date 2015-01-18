package cz.muni.fi.cdii.eclipse.inspection;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Collections;

import org.junit.Test;

import cz.muni.fi.cdii.common.model.Model;

public class GraphInspectionTest {

	@Test
	public void testGetTask() {
		final InspectionTask task = mock(InspectionTask.class);
		final Model model = mock(Model.class);
		doReturn(Collections.emptySet()).when(model).getBeans();
				
		final GraphInspection inspection = new GraphInspection(model, task);
		
		assertTrue("getTask", task == inspection.getTask());
	}

}

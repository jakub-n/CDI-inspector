package cz.muni.fi.cdii.eclipse;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import cz.muni.fi.cdii.eclipse.ui.e3.actions.TmpAction1;

//TODO delete
public class TmpSubscriber implements EventHandler {

    @Inject
    @Optional
    public void subscribe(@UIEventTopic(TmpAction1.EVENT) String data) {
        System.out.println("event recieved: " + data);
    }

    @Override
    public void handleEvent(Event event) {
        System.out.println("directly subscribet event received: " 
                + event.getProperty(IEventBroker.DATA));
    }
}

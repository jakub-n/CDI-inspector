package cz.muni.fi.cdii.webapp;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;

public class MyScopeContext implements Context {
	
	private Context internalContext;

	public MyScopeContext(Context internalContext) {
		this.internalContext = internalContext;
	}

	@Override
	public <T> T get(Contextual<T> contextual,
			CreationalContext<T> creationalContext) {
		return this.internalContext.get(contextual, creationalContext);
	}

	@Override
	public <T> T get(Contextual<T> contextual) {
		return internalContext.get(contextual);
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public Class<? extends Annotation> getScope() {
		return MyScoped.class;
	}
}

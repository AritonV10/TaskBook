package taskbook.v1.platform.CDI;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class BeanService {
	
	private static final BeanService beanService = new BeanService();
	
	BeanService(){}
	
	public static BeanService getInstance() {
		return beanService;
	}
	
	// we can't inject beans into ConstraintValidator's
	// so we have to do it manually
	public <T> T getCDIBean(final Class<T> clasz) {
		BeanManager beanManager = getBeanManager();
		if(beanManager != null) {
			Bean<T> beanOfType = (Bean<T>) beanManager.getBeans(clasz).iterator().next();
			CreationalContext<T> creationalContext = beanManager.createCreationalContext(beanOfType);
			return (T) beanManager.getReference(beanOfType, clasz, creationalContext);
		} else {
			try {
				return clasz.newInstance();
			} catch (IllegalAccessException | InstantiationException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public BeanManager getBeanManager() {
		CDI<Object> container;
		try {
			container = CDI.current();
			if(container != null) {
				return container.getBeanManager();
			} else {
				try {
					InitialContext initialContext = new InitialContext();
					return (BeanManager) initialContext.lookup("java:comp/BeanManager");
				} catch (NamingException e) {
					e.printStackTrace();
				}
				
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

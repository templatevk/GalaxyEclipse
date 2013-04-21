package arch.galaxyeclipse.shared.context;

import org.springframework.context.support.*;

/**
 * Holds the Spring xml context.
 */
public class ContextHolder {
	public static final ClassPathXmlApplicationContext INSTANCE =
			new ClassPathXmlApplicationContext("applicationContext.xml");

    public static <T> T getBean(Class<T> requiredType) {
        return INSTANCE.getBean(requiredType);
    }
}

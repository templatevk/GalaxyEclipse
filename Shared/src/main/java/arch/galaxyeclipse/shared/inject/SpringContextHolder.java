package arch.galaxyeclipse.shared.inject;

import org.springframework.context.support.*;

public class SpringContextHolder {
	public static final ClassPathXmlApplicationContext CONTEXT = 
			new ClassPathXmlApplicationContext("applicationContext.xml");
}

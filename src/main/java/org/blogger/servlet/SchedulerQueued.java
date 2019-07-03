package org.blogger.servlet;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.blogger.service.BloggerService;

@Component(configurationFactory = true)
@Service(java.lang.Runnable.class)
@Properties({ @Property(name="scheduler.expression" , value="0 0/30 * * * ?" ) })

 

public class SchedulerQueued implements Runnable {
	@Reference
    private BloggerService blogService;
	
		 public void run() {
			 	blogService.queuedPost();
		        System.out.println("Post Submit");
		    }

	
	 
}

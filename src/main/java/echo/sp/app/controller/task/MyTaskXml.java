package echo.sp.app.controller.task;

import org.springframework.stereotype.Component;

/**   
 * Scheduled
 * @author Ethan   
 * @date 2015年9月30日 
 */
@Component 
public class MyTaskXml {
	public void show(){  
        System.out.println("XMl:is show run");  
    }  
    public void print(){  
        System.out.println("XMl:print run");  
    }  
}

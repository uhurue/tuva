package org.superman;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class HelloBean
 */
@Stateless
@LocalBean
public class HelloBean {

    public String from()
    {
        return "EJB";
    }
}

package com.flipkart.hibernate.multisession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by rishabh.sood on 11/08/16.
 */
public class MultiManagedSessionContext extends ManagedSessionContext {

    public MultiManagedSessionContext(SessionFactoryImplementor factory) {
        super(factory);
    }

    public static List<SessionFactory> getBoundSessionFactories() {
        Map<SessionFactory, Session> sessionMap = sessionMap();
        if(sessionMap != null) {
            return new ArrayList<>(sessionMap().keySet());
        }
        return Collections.emptyList();
    }
}

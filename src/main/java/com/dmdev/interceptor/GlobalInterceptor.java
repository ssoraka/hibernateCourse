package com.dmdev.interceptor;

import org.hibernate.CallbackException;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;

public class GlobalInterceptor implements Interceptor {

//    можно сделать только 1 интерсептор в отличие от листенеров.
//    его можно либо глобально установить в sessionFactory либо при создании session
    @Override
    public boolean onFlushDirty(Object entity, Object id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
        return Interceptor.super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }
}

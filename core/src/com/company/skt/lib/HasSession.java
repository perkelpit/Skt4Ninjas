package com.company.skt.lib;

/** An {@code interface} for {@link StageScreen}-subclasses to be able to call this method without knowing which
 * {@code screen} it is.*/
public interface HasSession {
        void setSessionToNull();
}

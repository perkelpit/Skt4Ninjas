package com.company.skt.lib;

/** An {@code interface} for {@link StageScreen}-subclasses to ensure that they have those methods to handle
 * {@code events} and {@code clicks}.*/
public interface EventClickHandler {
    void click(String click);
    void event(String event);
}

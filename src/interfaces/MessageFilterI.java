package interfaces;

import jdk.nashorn.internal.objects.annotations.*;

/**
 * The interface MessageFilterI defines the interface
 * required to filter messages
 */
@FunctionalInterface
public interface MessageFilterI {
    boolean filter(MessageI m);
}

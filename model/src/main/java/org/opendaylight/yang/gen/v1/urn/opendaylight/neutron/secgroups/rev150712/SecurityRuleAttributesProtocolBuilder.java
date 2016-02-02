package org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.SecurityRuleAttributes.Protocol;


/**
 * The purpose of generated class in src/main/java for Union types is to create new instances of unions from a string representation.
 * In some cases it is very difficult to automate it since there can be unions such as (uint32 - uint16), or (string - uint32).
 *
 * The reason behind putting it under src/main/java is:
 * This class is generated in form of a stub and needs to be finished by the user. This class is generated only once to prevent
 * loss of user code.
 *
 */
public class SecurityRuleAttributesProtocolBuilder {

    public static Protocol getDefaultInstance(java.lang.String defaultValue) {
        try {
            return new Protocol(Short.valueOf(defaultValue));
        } catch (NumberFormatException e) {
            return new Protocol(defaultValue);
        }
    }
}

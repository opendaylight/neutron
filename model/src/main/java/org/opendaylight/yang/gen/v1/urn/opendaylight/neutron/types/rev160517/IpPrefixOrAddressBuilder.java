package org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.types.rev160517;


import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddressBuilder;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpPrefix;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpPrefixBuilder;


/**
 * The purpose of generated class in src/main/java for Union types is to create new instances of unions from a string representation.
 * In some cases it is very difficult to automate it since there can be unions such as (uint32 - uint16), or (string - uint32).
 *
 * The reason behind putting it under src/main/java is:
 * This class is generated in form of a stub and needs to be finished by the user. This class is generated only once to prevent
 * loss of user code.
 *
 */
public class IpPrefixOrAddressBuilder {
    public static IpPrefixOrAddress getDefaultInstance(final java.lang.String defaultValue) {
        try {
            IpPrefix ipPrefix = IpPrefixBuilder.getDefaultInstance(defaultValue);
            return new IpPrefixOrAddress(ipPrefix);
        } catch (IllegalArgumentException e) {
            IpAddress ipAddress = IpAddressBuilder.getDefaultInstance(defaultValue);
            return new IpPrefixOrAddress(ipAddress);
        }
    }
}

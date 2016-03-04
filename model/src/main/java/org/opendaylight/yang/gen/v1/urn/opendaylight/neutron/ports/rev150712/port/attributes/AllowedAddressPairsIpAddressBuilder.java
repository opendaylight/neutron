package org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev150712.port.attributes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev150712.port.attributes.AllowedAddressPairs.IpAddress;


/**
 * The purpose of generated class in src/main/java for Union types is to create new instances of unions from a string representation.
 * In some cases it is very difficult to automate it since there can be unions such as (uint32 - uint16), or (string - uint32).
 *
 * The reason behind putting it under src/main/java is:
 * This class is generated in form of a stub and needs to be finished by the user. This class is generated only once to prevent
 * loss of user code.
 *
 */
public class AllowedAddressPairsIpAddressBuilder {
    public static IpAddress getDefaultInstance(java.lang.String defaultValue) {
        org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress ipAddress =
                new org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress(defaultValue.toCharArray());
        return new IpAddress(ipAddress);
    }
}

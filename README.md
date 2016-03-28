# Override DNS A-Record in Java

Suppose your code needs to connect to "api.company.com" for accessing services.
The "api.company.com" is hardcoded in a jar which you cannot override directly.

During testing, the code should be tested against "apitests.company.com" for mock services.
Since the jar has hardcoded values, it will only connect to "api.company.com".

In such a situation, it is possible have a DNS A Record embedded in the JVM, where "api.company.com" points to IP address of "apitests.company.com"
This doesn't change /etc/hosts file or nameserver settings.
Simply the namelookup layer in the JVM layer has a different ARecord for "api.company.com".

Please see the class DnsTest for details of overriding.

https://github.com/snambi/dns-ar-override/blob/master/src/main/java/org/github/dns/DnsTest.java



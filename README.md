# Zimbra User Alias
With this extension you can allow a user, users in a cos or users in a domain to create Alias on their own account. For Zimbra 8.8.11 and above only. When adding/removing Alias, the Zimlet will also add/remove personas so the user is good to go!

![Screenshot](https://raw.githubusercontent.com/Zimbra-Community/user-alias/master/documentation/screenshot.png)

### Installation
    wget https://raw.githubusercontent.com/Zimbra-Community/user-alias/master/user-alias-installer.sh -O /tmp/user-alias-installer.sh
    chmod +rx /tmp/user-alias-installer.sh
    /tmp/user-alias-installer.sh
    
### Configuration
After running the installer, you must assign rights in `/opt/zimbra/lib/ext/userAlias/config.properties`, example:

    aliasLimit=10
    aliasLimit-barrydegraaff.tk=1010    
    allowUser=admin@zimbradev.barrydegraaff.tk
    allowUserDomains=allowdomainX.com,allowdomainP.com
    default=allowdomainX.com,allowdomainY.com,example.com,barrydegraaff.tk
    defaultExternal=
    otherCos=domainZ.com
    domainZ.com=allowdomainX.com
    mycos=example.gnd
    zimbradev.barrydegraaff.tk=example.com,barrydegraaff.tk

You can put domains and cos'es in this file. But you can also give selected users more access by using allowUser and allowUserDomains.

So for the user admin@zimbradev.barrydegraaff.tk who is in the `defaul`t cos, the following would apply:

allowUser and allowUserDomains: allows admin@zimbradev.barrydegraaff.tk to create/remove alias in allowdomainX.com,allowdomainP.com. admin@zimbradev.barrydegraaff.tk can also create/remove alias in example.com,barrydegraaff.tk because of the domain (zimbradev.barrydegraaff.tk) and also create/remove alias in allowdomainX.com,allowdomainY.com,example.com,barrydegraaff.tk because the admin@zimbradev.barrydegraaff.tk is in default cos.

You can define a maximum number of Alias for each domain, aliasLimit applies to all users that do not have a per-domain Limit.


========================================================================

### License

Copyright (C) 2018-2021  Barry de Graaff A [Zeta Alliance](https://zetalliance.org/) Zimlet

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see http://www.gnu.org/licenses/.

# Zimbra User Alias
With this extension you can allows a user, users in a cos or users in a domain to create Alias on their own account. For Zimbra 8.7.11 and above only. When adding/removing Alias, the Zimlet will also add/remove personas so the user is good to go!

![Screenshot](https://raw.githubusercontent.com/Zimbra-Community/user-alias/master/documentation/screenshot.png)

### Installation
    wget https://raw.githubusercontent.com/Zimbra-Community/user-alias/master/user-alias-installer.sh -O /tmp/user-alias-installer.sh
    chmod +rx /tmp/user-alias-installer.sh
    /tmp/user-alias-installer.sh
    
### Configuration
After running the installer, you must assign rights in `/opt/zimbra/lib/ext/userAlias/config.properties`, example:

    aliasLimit=10
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

The aliasLimit applies to all users, using this extension.

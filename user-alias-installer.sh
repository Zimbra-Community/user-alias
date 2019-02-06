#!/bin/bash

#!/bin/bash

# Copyright (C) 2017-2018 Barry de Graaff
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 2 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see http://www.gnu.org/licenses/.

set -e
# if you want to trace your script uncomment the following line
# set -x

# Make sure only root can run our script
if [ "$(id -u)" != "0" ]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

TMPFOLDER="$(mktemp -d /tmp/user-alias-installer.XXXXXXXX)"
cd $TMPFOLDER
git clone --depth=1 https://github.com/Zimbra-Community/user-alias
cd user-alias

rm -Rf /opt/zimbra/lib/ext/userAlias
mkdir -p /opt/zimbra/lib/ext/userAlias

cp extension/out/artifacts/userAlias_jar/userAlias.jar /opt/zimbra/lib/ext/userAlias/
cp config.properties /opt/zimbra/lib/ext/userAlias/

rm -Rf $TMPFOLDER


echo "--------------------------------------------------------------------------------------------------------------
User Alias Extension installed successful

To activate your configuration, run as zimbra user:
su - zimbra -c \"zmmailboxdctl restart\"

You must set-up your permissions in /opt/zimbra/lib/ext/userAlias/config.properties or this extension won't do much.

Please deploy the Zimlet yourself:
    cd /tmp
    wget --no-cache https://github.com/Zimbra-Community/user-alias/releases/download/0.0.2/tk_barrydegraaff_useralias.zip -O /tmp/tk_barrydegraaff_useralias.zip
    su zimbra
    
    cd /tmp
    zmzimletctl deploy tk_barrydegraaff_useralias.zip
--------------------------------------------------------------------------------------------------------------
"


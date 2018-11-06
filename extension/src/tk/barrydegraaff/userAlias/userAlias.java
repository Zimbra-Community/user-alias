/*

Copyright (C) 2018  Barry de Graaff

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see http://www.gnu.org/licenses/.

*/
package tk.barrydegraaff.userAlias;

import java.util.Map;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.Element;
import com.zimbra.cs.account.Account;
import com.zimbra.cs.account.Cos;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.soap.DocumentHandler;
import com.zimbra.soap.ZimbraSoapContext;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/*

Example request from the browser:
      var soapDoc = AjxSoapDoc.create("userAlias", "urn:userAlias", null);
      var params = {
         soapDoc: soapDoc,
         asyncMode: true,
         callback:null
      };
      soapDoc.getMethod().setAttribute("action", "getAllowedAlias");
      appCtxt.getAppController().sendRequest(params);



      var soapDoc = AjxSoapDoc.create("userAlias", "urn:userAlias", null);
      var params = {
         soapDoc: soapDoc,
         asyncMode: true,
         callback:null
      };
      soapDoc.getMethod().setAttribute("action", "createAlias");
      soapDoc.getMethod().setAttribute("alias", "info@example.com");
      appCtxt.getAppController().sendRequest(params);


      var soapDoc = AjxSoapDoc.create("userAlias", "urn:userAlias", null);
      var params = {
         soapDoc: soapDoc,
         asyncMode: true,
         callback:null
      };
      soapDoc.getMethod().setAttribute("action", "removeAlias");
      soapDoc.getMethod().setAttribute("alias", "info@st.com");
      appCtxt.getAppController().sendRequest(params);



*/

public class userAlias extends DocumentHandler {
    public Element handle(Element request, Map<String, Object> context)
            throws ServiceException {
        try {
            ZimbraSoapContext zsc = getZimbraSoapContext(context);

            //Get the User Account and CoS
            Account acct = getRequestedAccount(zsc);
            String username = acct.getName();
            Cos cos = acct.getCOS();
            String COS = cos.getName();


            Element response = zsc.createElement(
                    "response"
            );
            switch (request.getAttribute("action")) {
                case "getAllowedAlias":
                    return getAllowedAlias(request, response, username, COS);
                case "getAlias":
                    String[] alias = acct.getMailAlias();
                    //return getAlias(request, response, username, COS);
                    response.setText(String.join(",", alias));
                    break;
                case "createAlias":
                    if (checkPermission(username, COS, request.getAttribute("alias")) && validEmail(request.getAttribute("alias")) && validEmail(username)) {
                        response.setText("end of command");
                        Provisioning prov = Provisioning.getInstance();
                        prov.addAlias(acct, request.getAttribute("alias"));
                        break;
                    } else {
                        response.setText("Permission denied or wrong email address");
                        break;
                    }
                case "removeAlias":
                    if (checkPermission(username, COS, request.getAttribute("alias")) && validEmail(request.getAttribute("alias")) && validEmail(username)) {
                        response.setText("end of command");
                        Provisioning prov = Provisioning.getInstance();
                        prov.removeAlias(acct, request.getAttribute("alias"));
                        break;
                    } else {
                        response.setText("Permission denied or wrong email address");
                        break;
                    }
            }
            return (response);
        } catch (
                Exception e) {
            throw ServiceException.FAILURE(e.toString(), e);
        }
    }

    /**
     * List the users current Alias'es and permissions, so the Zimlet knows what Alias domains are available to modify
     */
    private Element getAllowedAlias(Element request, Element response, String username, String cos) {
        response.setText(getAllowedAlias(username, cos));
        return response;
    }

    /*Alias Limit is only enforced in the Zimlet*/
    public static boolean checkPermission(String username, String cos, String alias) {
        try {
            FileInputStream input = new FileInputStream("/opt/zimbra/lib/ext/userAlias/config.properties");
            Properties prop = new Properties();
            prop.load(input);

            String[] allowedByCOS = null;
            try {
                allowedByCOS = prop.getProperty(cos).split(",");
            } catch (Exception e) {
            }
            String[] allowedByUsernameDomains = null;
            try {
                allowedByUsernameDomains = prop.getProperty("allowUserDomains").split(",");
            } catch (Exception e) {
            }
            String[] allowedByUsername = null;
            try {
                allowedByUsername = prop.getProperty("allowUser").split(",");
            } catch (Exception e) {
            }

            String[] allowedByDomain = null;
            try {
                allowedByDomain = prop.getProperty(getDomainByEmail(username)).split(",");
            } catch (Exception e) {
            }

            try {
                if (Arrays.asList(allowedByUsername).contains(username)) {
                    if (Arrays.asList(allowedByUsernameDomains).contains(getDomainByEmail(alias))) {
                        System.out.print(username + " allowed to create alias for " + alias + " based on allowUser directive.\r\n");
                        return true;
                    }
                }
            } catch (Exception e) {
            }

            try {
                if (Arrays.asList(allowedByCOS).contains(getDomainByEmail(alias))) {
                    System.out.print(username + " allowed to create alias for " + alias + " based on COS " + cos + ".\r\n");
                    return true;
                }
            } catch (Exception e) {
            }

            try {
                if (Arrays.asList(allowedByDomain).contains(getDomainByEmail(alias))) {
                    System.out.print(username + " allowed to create alias for " + alias + " based on domain " + getDomainByEmail(username) + ".\r\n");
                    return true;
                }
            } catch (Exception e) {
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //String.join... Will work on Java 8 and above (and so Zimbra 8.7 and above and not 8.0 (dunno about 8.6))
    public static String getAllowedAlias(String username, String cos) {
        try {
            FileInputStream input = new FileInputStream("/opt/zimbra/lib/ext/userAlias/config.properties");
            Properties prop = new Properties();
            prop.load(input);

            String aliasLimit = prop.getProperty("aliasLimit");

            String[] allowedByCOS = new String[0];
            try {
                allowedByCOS = prop.getProperty(cos).split(",");
            } catch (Exception e) {
            }

            String[] allowedByUsernameDomains = null;
            try {
                allowedByUsernameDomains = prop.getProperty("allowUserDomains").split(",");
            } catch (Exception e) {
            }

            String[] allowedByDomain = new String[0];
            try {
                allowedByDomain = prop.getProperty(getDomainByEmail(username)).split(",");
            } catch (Exception e) {
            }

            return aliasLimit + "," + String.join(",", allowedByCOS) + "," + String.join(",", allowedByDomain) + "," + String.join(",", allowedByUsernameDomains);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDomainByEmail(final String email) {
        String domain = null;
        int index = -1;
        if ((index = email.indexOf("@")) > -1) {
            domain = email.substring(index + 1);
        }

        return domain.trim();
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    public static boolean validEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}

package tk.barrydegraaff.test;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        // write your code here
        if (checkPermission("info@barrydegraaff.tk", "default", "alias@example.com")) {
            System.out.print("true\r\n");
        } else {
            System.out.print("false\r\n");
        }

        System.out.print(getAllowedAlias("info@barrydegraaff.tk", "default", "alias@example.com"));


    }

    /*Alias Limit is only enforced in the Zimlet*/
    public static boolean checkPermission(String username, String cos, String alias) {
        try {
            FileInputStream input = new FileInputStream("/home/bar/git/userAlias/config.properties");
            Properties prop = new Properties();
            prop.load(input);

            String[] allowedByCOS = null;
            try {
                allowedByCOS = prop.getProperty(cos).split(",");
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
                    System.out.print(username + " allowed to create alias for " + alias + " based on allowUser directive.\r\n");
                    return true;
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

    public static String getAllowedAlias(String username, String cos, String alias) {
        try {
            FileInputStream input = new FileInputStream("/home/bar/git/userAlias/config.properties");
            Properties prop = new Properties();
            prop.load(input);

            String aliasLimit = prop.getProperty("aliasLimit");

            String[] allowedByCOS = new String[0];
            try {
                allowedByCOS = prop.getProperty(cos).split(",");
            } catch (Exception e) {
            }
            String[] allowedByUsername = new String[0];
            try {
                allowedByUsername = prop.getProperty("allowUser").split(",");
            } catch (Exception e) {
            }

            String[] allowedByDomain = new String[0];
            try {
                allowedByDomain = prop.getProperty(getDomainByEmail(username)).split(",");
            } catch (Exception e) {
            }

            try {
                if (Arrays.asList(allowedByUsername).contains(username)) {
                    return aliasLimit + ",*";
                }
            } catch (Exception e) {
            }

            return aliasLimit + "," + String.join(",", allowedByCOS) + "," + String.join(",", allowedByDomain);
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

}



package ictclogin;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by Ali on 11/29/2017.
 */

public class Ictclogin_functions {

    public static String doLogout() {
        try {
            Jsoup.connect("http://ictc.hs/logout").get();
            //Toast.makeText(context, "Logout Request Sent", Toast.LENGTH_SHORT).show();
            return "Logout Request Sent";
        } catch (Exception e){
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return e.getMessage();
        }
    }

    /*public static String killUser(String username, String password){
        try {

            //grab login form page first
            Connection.Response loginPageResponse =
                    Jsoup.connect("http://account.shirazu.ac.ir/IBSng/user")
                            .referrer("http://account.shirazu.ac.ir/IBSng/user")
                            .userAgent("Mozilla/5.0")
                            .timeout(10 * 1000)
                            .followRedirects(true)
                            .execute();

            System.out.println("Fetched login page");

            //get the cookies from the response, which we will post to the action URL
            Map<String, String> mapLoginPageCookies = loginPageResponse.cookies();

            //lets make data map containing all the parameters and its values found in the form
            Map<String, String> mapParams = new HashMap<String, String>();
            mapParams.put("normal_username", username);
            mapParams.put("normal_password", password);

            //URL found in form's action attribute
            String strActionURL = "http://account.shirazu.ac.ir/IBSng/user";

            Connection.Response responsePostLogin = Jsoup.connect(strActionURL)
                    //referrer will be the login page's URL
                    .referrer("http://account.shirazu.ac.ir/IBSng/user")
                    //user agent
                    .userAgent("Mozilla/5.0")
                    //connect and read time out
                    .timeout(10 * 1000)
                    //post parameters
                    .data(mapParams)
                    //cookies received from login page
                    .cookies(mapLoginPageCookies)
                    //many websites redirects the user after login, so follow them
                    .followRedirects(true)
                    .execute();


            mapLoginPageCookies = responsePostLogin.cookies();

            mapParams = new HashMap<>();
            mapParams.put("kill_me", "1");

            Jsoup.connect("http://account.shirazu.ac.ir/IBSng/user/home.php")
                    //referrer will be the login page's URL
                    .referrer("http://account.shirazu.ac.ir/IBSng/user/")
                    //user agent
                    .userAgent("Mozilla/5.0")
                    //connect and read time out
                    .timeout(10 * 1000)
                    //post parameters
                    .data(mapParams)
                    //cookies received from login page
                    .cookies(mapLoginPageCookies)
                    //many websites redirects the user after login, so follow them
                    .followRedirects(true)
                    .post();

            //parse the document from response
            Document document = responsePostLogin.parse();

            //get the cookies
            Map<String, String> mapLoggedInCookies = responsePostLogin.cookies();

            /*
             * For all the subsequent requests, you need to send
             * the mapLoggedInCookies containing cookies


        } catch (Exception e){
            return e.toString();
        }
        return "User killed successfully";
    }*/

    public static String doLogin(String username, String password){
        if(username.isEmpty())
            return "Username is empty";
        try {
            //Toast.makeText(context, "Getting ictc...", Toast.LENGTH_SHORT).show();
            Document doc = Jsoup.connect("http://ictc.hs").get();
            String chapID = doc.select("input[name=chap-id]").get(0).val();
            String chapChallenge = doc.select("input[name=chap-challenge]").get(0).val();

            Connection data1 = Jsoup.connect("http://ictc.hs/login").data("username", username).data("password", hexMD5(chapID + password + chapChallenge)).data("chap-challenge", chapChallenge).data("chap-id", chapID).data("system-code", doc.select("input[name=system-code]").get(0).val()).data("dst", doc.select("input[name=dst]").get(0).val()).data("popup", doc.select("input[name=popup]").get(0).val()).data("error-code", doc.select("input[name=error_code]").get(0).val());

            Elements temp = doc.select("input[name=system-server]");
            if(temp.size() > 0) {
                data1.data("system-server", temp.get(0).val());
            }

            doc = data1.get();
            String error = doc.select("input[name=error_code]").get(0).val();
            if(error.equals("")) {
                //Toast.makeText(context, "Login Request Sent", Toast.LENGTH_SHORT).show();
                return "Login Request Sent";
            }
            else {
                return getErrorText(error);
            }
        }
        catch (Exception e) {
            if(e.getMessage().equals("Index: 0, Size: 0")) {
                //Toast.makeText(context, "Currently logged in", Toast.LENGTH_SHORT).show();
                return "Currently logged in";
            }
            else {
                //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                return e.getMessage();
            }
        }
    }

    private static String hexMD5(String input) throws ScriptException {
        final String script =
                "function safe_add(x, y)\n" +
                        "{\n" +
                        "  var lsw = (x & 0xFFFF) + (y & 0xFFFF)\n" +
                        "  var msw = (x >> 16) + (y >> 16) + (lsw >> 16)\n" +
                        "  return (msw << 16) | (lsw & 0xFFFF)\n" +
                        "}\n" +
                        "\n" +
                        "function cmn(q, a, b, x, s, t)\n" +
                        "{\n" +
                        "  return safe_add(rol(safe_add(safe_add(a, q), safe_add(x, t)), s), b)\n" +
                        "}\n" +
                        "function ff(a, b, c, d, x, s, t)\n" +
                        "{\n" +
                        "  return cmn((b & c) | ((~b) & d), a, b, x, s, t)\n" +
                        "}\n" +
                        "function gg(a, b, c, d, x, s, t)\n" +
                        "{\n" +
                        "  return cmn((b & d) | (c & (~d)), a, b, x, s, t)\n" +
                        "}\n" +
                        "function hh(a, b, c, d, x, s, t)\n" +
                        "{\n" +
                        "  return cmn(b ^ c ^ d, a, b, x, s, t)\n" +
                        "}\n" +
                        "function ii(a, b, c, d, x, s, t)\n" +
                        "{\n" +
                        "  return cmn(c ^ (b | (~d)), a, b, x, s, t)\n" +
                        "}\n" +
                        "\n" +
                        "function coreMD5(x)\n" +
                        "{\n" +
                        "  var a =  1732584193\n" +
                        "  var b = -271733879\n" +
                        "  var c = -1732584194\n" +
                        "  var d =  271733878\n" +
                        "\n" +
                        "  for(i = 0; i < x.length; i += 16)\n" +
                        "  {\n" +
                        "    var olda = a\n" +
                        "    var oldb = b\n" +
                        "    var oldc = c\n" +
                        "    var oldd = d\n" +
                        "\n" +
                        "    a = ff(a, b, c, d, x[i+ 0], 7 , -680876936)\n" +
                        "    d = ff(d, a, b, c, x[i+ 1], 12, -389564586)\n" +
                        "    c = ff(c, d, a, b, x[i+ 2], 17,  606105819)\n" +
                        "    b = ff(b, c, d, a, x[i+ 3], 22, -1044525330)\n" +
                        "    a = ff(a, b, c, d, x[i+ 4], 7 , -176418897)\n" +
                        "    d = ff(d, a, b, c, x[i+ 5], 12,  1200080426)\n" +
                        "    c = ff(c, d, a, b, x[i+ 6], 17, -1473231341)\n" +
                        "    b = ff(b, c, d, a, x[i+ 7], 22, -45705983)\n" +
                        "    a = ff(a, b, c, d, x[i+ 8], 7 ,  1770035416)\n" +
                        "    d = ff(d, a, b, c, x[i+ 9], 12, -1958414417)\n" +
                        "    c = ff(c, d, a, b, x[i+10], 17, -42063)\n" +
                        "    b = ff(b, c, d, a, x[i+11], 22, -1990404162)\n" +
                        "    a = ff(a, b, c, d, x[i+12], 7 ,  1804603682)\n" +
                        "    d = ff(d, a, b, c, x[i+13], 12, -40341101)\n" +
                        "    c = ff(c, d, a, b, x[i+14], 17, -1502002290)\n" +
                        "    b = ff(b, c, d, a, x[i+15], 22,  1236535329)\n" +
                        "\n" +
                        "    a = gg(a, b, c, d, x[i+ 1], 5 , -165796510)\n" +
                        "    d = gg(d, a, b, c, x[i+ 6], 9 , -1069501632)\n" +
                        "    c = gg(c, d, a, b, x[i+11], 14,  643717713)\n" +
                        "    b = gg(b, c, d, a, x[i+ 0], 20, -373897302)\n" +
                        "    a = gg(a, b, c, d, x[i+ 5], 5 , -701558691)\n" +
                        "    d = gg(d, a, b, c, x[i+10], 9 ,  38016083)\n" +
                        "    c = gg(c, d, a, b, x[i+15], 14, -660478335)\n" +
                        "    b = gg(b, c, d, a, x[i+ 4], 20, -405537848)\n" +
                        "    a = gg(a, b, c, d, x[i+ 9], 5 ,  568446438)\n" +
                        "    d = gg(d, a, b, c, x[i+14], 9 , -1019803690)\n" +
                        "    c = gg(c, d, a, b, x[i+ 3], 14, -187363961)\n" +
                        "    b = gg(b, c, d, a, x[i+ 8], 20,  1163531501)\n" +
                        "    a = gg(a, b, c, d, x[i+13], 5 , -1444681467)\n" +
                        "    d = gg(d, a, b, c, x[i+ 2], 9 , -51403784)\n" +
                        "    c = gg(c, d, a, b, x[i+ 7], 14,  1735328473)\n" +
                        "    b = gg(b, c, d, a, x[i+12], 20, -1926607734)\n" +
                        "\n" +
                        "    a = hh(a, b, c, d, x[i+ 5], 4 , -378558)\n" +
                        "    d = hh(d, a, b, c, x[i+ 8], 11, -2022574463)\n" +
                        "    c = hh(c, d, a, b, x[i+11], 16,  1839030562)\n" +
                        "    b = hh(b, c, d, a, x[i+14], 23, -35309556)\n" +
                        "    a = hh(a, b, c, d, x[i+ 1], 4 , -1530992060)\n" +
                        "    d = hh(d, a, b, c, x[i+ 4], 11,  1272893353)\n" +
                        "    c = hh(c, d, a, b, x[i+ 7], 16, -155497632)\n" +
                        "    b = hh(b, c, d, a, x[i+10], 23, -1094730640)\n" +
                        "    a = hh(a, b, c, d, x[i+13], 4 ,  681279174)\n" +
                        "    d = hh(d, a, b, c, x[i+ 0], 11, -358537222)\n" +
                        "    c = hh(c, d, a, b, x[i+ 3], 16, -722521979)\n" +
                        "    b = hh(b, c, d, a, x[i+ 6], 23,  76029189)\n" +
                        "    a = hh(a, b, c, d, x[i+ 9], 4 , -640364487)\n" +
                        "    d = hh(d, a, b, c, x[i+12], 11, -421815835)\n" +
                        "    c = hh(c, d, a, b, x[i+15], 16,  530742520)\n" +
                        "    b = hh(b, c, d, a, x[i+ 2], 23, -995338651)\n" +
                        "\n" +
                        "    a = ii(a, b, c, d, x[i+ 0], 6 , -198630844)\n" +
                        "    d = ii(d, a, b, c, x[i+ 7], 10,  1126891415)\n" +
                        "    c = ii(c, d, a, b, x[i+14], 15, -1416354905)\n" +
                        "    b = ii(b, c, d, a, x[i+ 5], 21, -57434055)\n" +
                        "    a = ii(a, b, c, d, x[i+12], 6 ,  1700485571)\n" +
                        "    d = ii(d, a, b, c, x[i+ 3], 10, -1894986606)\n" +
                        "    c = ii(c, d, a, b, x[i+10], 15, -1051523)\n" +
                        "    b = ii(b, c, d, a, x[i+ 1], 21, -2054922799)\n" +
                        "    a = ii(a, b, c, d, x[i+ 8], 6 ,  1873313359)\n" +
                        "    d = ii(d, a, b, c, x[i+15], 10, -30611744)\n" +
                        "    c = ii(c, d, a, b, x[i+ 6], 15, -1560198380)\n" +
                        "    b = ii(b, c, d, a, x[i+13], 21,  1309151649)\n" +
                        "    a = ii(a, b, c, d, x[i+ 4], 6 , -145523070)\n" +
                        "    d = ii(d, a, b, c, x[i+11], 10, -1120210379)\n" +
                        "    c = ii(c, d, a, b, x[i+ 2], 15,  718787259)\n" +
                        "    b = ii(b, c, d, a, x[i+ 9], 21, -343485551)\n" +
                        "\n" +
                        "    a = safe_add(a, olda)\n" +
                        "    b = safe_add(b, oldb)\n" +
                        "    c = safe_add(c, oldc)\n" +
                        "    d = safe_add(d, oldd)\n" +
                        "  }\n" +
                        "  return [a, b, c, d]\n" +
                        "}\n" +
                        "\n" +
                        "function binl2hex(binarray)\n" +
                        "{\n" +
                        "  var hex_tab = '0123456789abcdef'\n" +
                        "  var str = ''\n" +
                        "  for(var i = 0; i < binarray.length * 4; i++)\n" +
                        "  {\n" +
                        "    str += hex_tab.charAt((binarray[i>>2] >> ((i%4)*8+4)) & 0xF) +\n" +
                        "           hex_tab.charAt((binarray[i>>2] >> ((i%4)*8)) & 0xF)\n" +
                        "  }\n" +
                        "  return str\n" +
                        "}\n" +
                        "\n" +
                        "function str2binl(str)\n" +
                        "{\n" +
                        "  var nblk = ((str.length + 8) >> 6) + 1 // number of 16-word blocks\n" +
                        "  var blks = new Array(nblk * 16)\n" +
                        "  for(var i = 0; i < nblk * 16; i++) blks[i] = 0\n" +
                        "  for(var i = 0; i < str.length; i++)\n" +
                        "    blks[i>>2] |= (str.charCodeAt(i) & 0xFF) << ((i%4) * 8)\n" +
                        "  blks[i>>2] |= 0x80 << ((i%4) * 8)\n" +
                        "  blks[nblk*16-2] = str.length * 8\n" +
                        "  return blks\n" +
                        "}\n" +
                        "\n" +
                        "function rol(num, cnt)\n" +
                        "{\n" +
                        "  return (num << cnt) | (num >>> (32 - cnt))\n" +
                        "}\n" +
                        "function hexMD5 (str) { return binl2hex(coreMD5( str2binl(str))) }\n" +
                        "\n" +
                        "hexMD5('" + input + "')";
        String result = "";
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        result = (String) engine.eval(script);

        return result;
    }


    public static String getErrorText(String text){
        if(text.equals("E=900 R=0 "))
            return "User not accessable or balance is finished.";
        else if(text.equals("E=901 R=0 "))
            return "Your account has been suspended by admin.";
        else if(text.equals("E=904 R=0 "))
            return "Your account has been expired";
        else if(text.equals("E=905 R=0 "))
            return "Your account has been expired";
        else if(text.equals("E=906 R=0 "))
            return "Account balance is finnished";
        else if(text.equals("E=907 R=0 "))
            return "Username or password is wrong";
        else if(text.equals("E=908 R=0 "))
            return "Account connection limit exceeded";
        else if(text.equals("E=909 R=0 "))
            return "You are not allowed to connect from here";
        else if(text.equals("E=915 R=0 "))
            return "Your weekly limit exceeded";
        else
            return text;
    }

}

AjxTemplate.register("tk_barrydegraaff_useralias.templates.preferences#Preferences", 
function(name, params, data, buffer) {
	var _hasBuffer = Boolean(buffer);
	data = (typeof data == "string" ? { id: data } : data) || {};
	buffer = buffer || [];
	var _i = buffer.length;

	buffer[_i++] = "<div id='tk_barrydegraaff_useralias_prefscreen'>\n";
	buffer[_i++] = "        Placeholder\n";
	buffer[_i++] = "    </div>";

	return _hasBuffer ? buffer.length : buffer.join("");
},
{
	"id": "Preferences"
}, true);
AjxPackage.define("tk_barrydegraaff_useralias.templates.preferences");
AjxTemplate.register("tk_barrydegraaff_useralias.templates.preferences", AjxTemplate.getTemplate("tk_barrydegraaff_useralias.templates.preferences#Preferences"), AjxTemplate.getParams("tk_barrydegraaff_useralias.templates.preferences#Preferences"));


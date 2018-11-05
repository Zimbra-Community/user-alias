/*
 Copyright (C) 2017-2018  Barry de Graaff

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
 */

function tk_barrydegraaff_useralias_HandlerObject() {
   tk_barrydegraaff_useralias_HandlerObject.settings = {};
};

tk_barrydegraaff_useralias_HandlerObject.prototype = new ZmZimletBase();
tk_barrydegraaff_useralias_HandlerObject.prototype.constructor = tk_barrydegraaff_useralias_HandlerObject;
var UserAliasZimlet = tk_barrydegraaff_useralias_HandlerObject;

UserAliasZimlet.prototype.init = function () {
    var section = {
        title: 'My Title',
        icon: 'TrustedAddresses',
        templateId: 'tk_barrydegraaff_useralias.templates.preferences' + '#Preferences',
        priority: 49,
        manageDirty: true,
        prefs: [
            'MySetting',
        ],
        createView: function(parent, sectionObj, controller) {
            return new UserAliasZimletPrefs(parent, sectionObj, controller, appCtxt._zimletMgr.getZimletByName('tk_barrydegraaff_useralias').handlerObject);
         }
    };
    ZmPref.registerPrefSection('USERALIAS_PREFERENCES', section);
};

/* status method show a Zimbra status message
 * */
UserAliasZimlet.prototype.status = function(text, type) {
   var transitions = [ ZmToast.FADE_IN, ZmToast.PAUSE, ZmToast.FADE_OUT ];
   appCtxt.getAppController().setStatusMsg(text, type, null, transitions);
}; 

UserAliasZimlet.prototype._cancelBtn =
function() {
   var zimletInstance = appCtxt._zimletMgr.getZimletByName('tk_barrydegraaff_useralias').handlerObject;
   
   try{
      zimletInstance._dialog.setContent('');
      zimletInstance._dialog.popdown();
   }
   catch (err) {}
};

UserAliasZimletPrefs = function(shell, section, controller, handler) {
    if (!arguments.length) return;
    this._createHtml = function(){return document.createTextNode("CLICK ME");};
    this.getHtmlElement = function(){return document.createElement("BUTTON");};
    this.setScrollStyle = function(){return 'none';};
    this.getHTMLElId = function(){return 'none';};
    this.hideMe = function(){return;};
    this.resetSize = function(){return;};
    this.setVisible = function(){return;};
    this.showMe = function(){console.log('showme');return;};
    this.getTabGroupMember = function(){return;};
    this.hasResetButton = function(){return false;};
    this.getTitle = function(){return 'User Alias';};
    this._handler = handler;
    ZmPreferencesPage.call(this, shell, section, controller);
    this._id = this.getHTMLElId();
    
};

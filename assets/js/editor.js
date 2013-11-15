(function(win){
	
	win.toolbar = {
		show:function(){
			win.nativetoolbar.show();
		},
		hide:function(){
			win.nativetoolbar.hide();
		},
		openKeyboard:function(){
			win.nativetoolbar.openKeyboard();
		},
		callback:{
			insertImage:function(base64){}
		}
	};
	
	win.nativetoolbarCallback = function(base64){
		toolbar.callback.insertImage(base64);
	}
	
	
	
	
})(window);

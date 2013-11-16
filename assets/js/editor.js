(function(win){
	
	win.toolbar = {
		show:function(){
			win.nativetoolbar.show();
		},
		hide:function(){
			win.nativetoolbar.hide();
		},
		openKeyboard:function(){
			setTimeout(function(){
				win.nativetoolbar.openKeyboard();
			},500);
		},
		callback:{
			insertImage:function(base64){},
			submit:function(){}
		}
	};
	
	win.nativetoolbarCallback = function(base64){
		toolbar.callback.insertImage(base64);
	}
	
	win.nativetoolbarSubmit = function(){
		var result = toolbar.callback.submit();
		win.nativetoolbar.submit(result);
	}
	
	
	
	
})(window);

(function(win){
	
	win.toolbar = {
		fullscreen:function(){
			win.nativetoolbar.fullscreen();
		},
		quitFullscreen:function(){
			win.nativetoolbar.quitFullscreen();
		},
		openKeyboard:function(){
			setTimeout(function(){
				win.nativetoolbar.openKeyboard();
			},500);
		},
		callback:{
			insertImage:function(base64){},
			insertFace:function(base64){},
			submit:function(){}
		}
	};
	
	win.nativetoolbarCallbackFace = function(base64){
		toolbar.callback.insertFace(base64);
	}
	
	win.nativetoolbarCallback = function(base64){
		toolbar.callback.insertImage(base64);
	}
	
	win.nativetoolbarSubmit = function(){
		var result = toolbar.callback.submit();
		win.nativetoolbar.submit(result);
	}
	
	
	
	
})(window);

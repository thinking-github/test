

var demo = function (id){
	
	return new demo.prototype.init(id);
}

 demo.prototype = {
		version:"002",
		chen:function(){}	
}

var init = demo.prototype.init = function(id){
	var kk = document.getElementById(id);
	this[0] = kk;
	return  this;
	
}
init.prototype =  demo.prototype

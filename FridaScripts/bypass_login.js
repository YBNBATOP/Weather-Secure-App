// Frida script to bypass login credential check
Java.perform(function () {
  
const target = Java.use('com.example.weatherapp.ui.login.LoginScreenKt')
	target.checkLoginCredentials.implementation = function(x,y,z)
{
	console.log("Bypassing login");
	return true
    };
});

